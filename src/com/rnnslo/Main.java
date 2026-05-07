package com.rnnslo;

import java.io.IOException;
import java.nio.file.Path;

import com.rnnslo.adapter.AdaptiveThreadPoolAdapter;
import com.rnnslo.adapter.ThreadPoolAdapter;
import com.rnnslo.config.ControllerConfiguration;
import com.rnnslo.control.Algorithm2RNNSLOControlLoop;
import com.rnnslo.decision.ResourceCostFunction;
import com.rnnslo.decision.SLOConstrainedDecisionModule;
import com.rnnslo.logging.CsvMetricLogger;
import com.rnnslo.metrics.HistoryBuffer;
import com.rnnslo.metrics.MetricsCollector;
import com.rnnslo.model.RecurrentPredictionModel;
import com.rnnslo.range.CandidatePoolSizeRange;
import com.rnnslo.server.ServerSimulator;
import com.rnnslo.server.WorkerThreadPool;
import com.rnnslo.training.Algorithm1TrainRNNModel;
import com.rnnslo.training.SyntheticOfflineDataCollection;
import com.rnnslo.training.TrainingDataset;
import com.rnnslo.workload.EvaluationWorkloadGenerator;
import com.rnnslo.workload.WorkloadGenerator;

public class Main {
    public static void main(String[] args) throws IOException {
        ControllerConfiguration configuration = ControllerConfiguration.forCurrentMachine()
                .withHistoryLength(30)
                .withP99SloMillis(1000.0)
                .withSamplingPeriodSeconds(1)
                .withControlIntervalSeconds(1);

        System.out.println("RNN-SLO Controller JavaSE-17 demo");
        System.out.println("Hardware threads detected: " + configuration.getHardwareThreads());
        System.out.println("Candidate pool range: " + configuration.getMinPoolSize() + ".." + configuration.getMaxPoolSize());
        System.out.println("History length: " + configuration.getHistoryLength());
        System.out.println("P99 SLO: " + configuration.getP99SloMillis() + " ms");

        TrainingDataset trainingDataset = SyntheticOfflineDataCollection.collect(configuration, 720, 20260507L);
        Algorithm1TrainRNNModel trainer = new Algorithm1TrainRNNModel(configuration);
        RecurrentPredictionModel trainedModel = trainer.train(trainingDataset, 6, 64);

        WorkerThreadPool workerThreadPool = new WorkerThreadPool(configuration.initialPoolSize());
        ThreadPoolAdapter threadPoolAdapter = new AdaptiveThreadPoolAdapter(workerThreadPool, configuration);
        WorkloadGenerator workloadGenerator = new EvaluationWorkloadGenerator(42L);
        ServerSimulator serverSimulator = new ServerSimulator(configuration, workloadGenerator, threadPoolAdapter);
        MetricsCollector metricsCollector = new MetricsCollector(serverSimulator);
        HistoryBuffer historyBuffer = HistoryBuffer.withDefaultMetrics(configuration);
        CandidatePoolSizeRange candidateRange = CandidatePoolSizeRange.fromConfiguration(configuration);
        SLOConstrainedDecisionModule decisionModule = new SLOConstrainedDecisionModule(
                configuration,
                candidateRange,
                ResourceCostFunction.equalWeights());

        Path logPath = Path.of("rnn_slo_run.csv");
        try (CsvMetricLogger logger = new CsvMetricLogger(logPath)) {
            Algorithm2RNNSLOControlLoop controlLoop = new Algorithm2RNNSLOControlLoop(
                    configuration,
                    metricsCollector,
                    historyBuffer,
                    trainedModel,
                    decisionModule,
                    threadPoolAdapter,
                    logger);

            controlLoop.runForSteps(2400);
        }

        System.out.println("Run finished. CSV written to: " + logPath.toAbsolutePath());
    }
}
