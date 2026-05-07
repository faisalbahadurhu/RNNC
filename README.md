# 🧠 RNN Controller 

**RNN Controller** This project presents a sequence-aware RNN-based controller for dynamic thread-pool management in server systems. The goal is to automatically adjust the number of worker threads at runtime in response to changing and bursty workloads, while maintaining performance guarantees.

---

## 🚀 Features
- 📊 Sequence-aware decision making using GRU to capture workload trends over time
- ⚙️ Dynamic thread-pool tuning instead of static configuration
- 🎯 SLO-aware optimization, prioritizing tail latency (P99) guarantees
- 🧠 Multi-metric prediction, including latency, CPU usage, and memory usage
- ⚖️ Safe selection strategy that filters configurations violating SLO constraints
- 🚀 Resource-efficient control, balancing performance and system cost
- 🔁 Adaptive behavior under bursty workloads without manual tuning
