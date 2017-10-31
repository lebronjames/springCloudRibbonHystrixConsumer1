Ribbon中引入Hystrix，实现消费者断路器功能
1) pom.xml中除了增加Ribbon(spring-cloud-starter-ribbon)、eureka(spring-cloud-starter-eureka)配置之外，
增加断路器Hystrix(spring-cloud-starter-hystrix)依赖
2) 在eureka-ribbon的主类RibbonApplication中使用@EnableCircuitBreaker注解开启断路器功能：
@SpringBootApplication
@EnableDiscoveryClient
@EnableCircuitBreaker
public class RibbonApplication {
	@Bean
	@LoadBalanced
	RestTemplate restTemplate() {
		return new RestTemplate();
	}
	public static void main(String[] args) {
		SpringApplication.run(RibbonApplication.class, args);
	}
}
3)新增ComputeService类，在使用ribbon消费服务的函数上增加@HystrixCommand注解来指定回调方法。
@Service
public class ComputeService {
    @Autowired
    RestTemplate restTemplate;
    @HystrixCommand(fallbackMethod = "addServiceFallback")
    public String addService() {
        return restTemplate.getForEntity("http://COMPUTE-SERVICE/add?a=10&b=20", String.class).getBody();
    }
    public String addServiceFallback() {
        return "error";
    }
}
4)提供rest接口的Controller改为调用ComputeService的addService
@RestController
public class ConsumerController {
    @Autowired
    private ComputeService computeService;
    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String add() {
        return computeService.addService();
    }
}
5) application.properties中配置eureka服务注册中心
spring.application.name=ribbon-hystrix-consumer
server.port=3533
eureka.client.serviceUrl.defaultZone=http://localhost:1111/eureka/


Ribbon增加断路器Hystrix请求服务地址：
http://10.5.2.241:3533/add