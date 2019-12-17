# 12-factor

The Twelve-Factor App

​    为了构建分布式微服务程序，能够部署到所有云服务，Heroku工程师总结所有云原生应用程序的12要素：



- **1.基准代码** **Single codebase**: 

  > The application must have one codebase, tracked in revision control for every application (read: microservice) that can be deployed multiple times (development, test, staging, and production environments). Two microservices do not share the same codebase. This model allows the flexibility to change and deploy services without impacting other parts of the application.

代码管理.git,svn



- **2.依赖 Dependencies**: 

- > The application must explicitly declare its code dependencies and add them to the application or microservice. The dependencies are packaged as part of the microservice JAR/WAR file. This helps isolate dependencies across microservices and reduce any side effects through multiple versions of the same JAR.

  依赖,maven  

  

- **3.配置 Config**: The application configuration data is moved out of the application or microservice and externalized through a configuration management tool. The application or microservice will pick up the configuration based on the environment in which it is running, allowing the same deployment unit to be propagated across the environments.

  - 外部化配置: 非代码修改配置,环境,运行命令等
- 内部化配置:代码修改配置

- **4.后端服务 Backing services**: All external resources, access should be an addressable URL. For example, SMTP URL, database URL, service HTTP URL, queue URL, and TCP URL. This allows URLs to be externalized to the config and managed for every environment.

  

- **5.构建，发布，运行 Build, release, and run**: The entire process of building, releasing, and running is treated as three separate steps. This means that, as part of the build, the application is built as an immutable entity. This immutable entity will pick the relevant configuration to run the process based on the environment (development, testing, staging, or production).

- **6.进程 Processes**: The microservice is built on and follows the shared-nothing model. This means the services are stateless and the state is externalized to either a cache or a data store. This allows seamless scalability and allows load balance or proxy to send requests to any of the instances of the service.

  

- **7.端口绑定 Port binding**: The microservice is built within a container. The service will export and bind all its interfaces through ports (including HTTP).

  

- **8.并发 Concurrency**: The microservice process is scaled out, meaning that, to handle increased traffic, more microservice processes are added to the environment. Within the microservice process, one can make use of the reactive model to optimize the resource utilization.

  

- **9.易处理 Disposability**: The idea is to build a microservice as immutable with a single responsibility to, in turn, maximize robustness with faster boot-up times. Immutability also lends to the service disposability.

  

- **10.环境等价Dev/prod parity**: The environments across the application life cycle—DEV, TEST, STAGING, and PROD—are kept as similar as possible to avoid any surprises later.

  

- **11.日志Logs**: Within the immutable microservice instance, the logs generated as part of the service processing are candidates for state. These logs should be treated as event streams and pushed out to a log aggregator infrastructure.

  

- **12.管理进程 Admin processes**: The microservice instances are long-running processes that continue unless they are killed or replaced with newer versions. All other admin and management tasks are treated as one-off processes: Applications that follow the 12 factors make no assumptions about the external environment, allowing them to be deployed on any cloud provider platform. This allows the same set of tools/processes/scripts to be run across environments and deploy distributed microservices applications in a consistent manner.