# 阿里巴巴2019中间件性能挑战赛-自适应负载均衡（初赛）赛题
## 背景
传统的负载均衡场景为单调度器模式，即中心化负载均衡：调度器负责将新到的请求立即转发至多个后端服务器中的一个。随着分布式系统的发展，这种单调度器模式在扩展性、可靠性和性能方面的问题愈发严重。因此，设计和实现去中心化且性能优异的负载均衡是学术和工业界的共同需求。

1. Gateway维护一个ConcurrentLinkedQueue[WorkRequest]，其中WorkRequest里面记录Provider的唯一标志。之后LoadBalance从这个队列里面一直Poll工作请求。如果有则选择对应的Provider打过去，如果没有则循环等待(此时称Provider端对Gateway产生了回压);
2. Provider端注册Listener的时候，告知Gateway其最大线程数能力maxThreads；然后Gateway那端的Listener接收到信息后，发送maxThreads数量大小的WorkRequest到 1 中的队列；
3. 每次请求完成之后，将WorkRequest还给队列。