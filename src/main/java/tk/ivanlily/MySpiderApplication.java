package tk.ivanlily;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
@MapperScan("tk.ivanlily.mapper")
public class MySpiderApplication implements CommandLineRunner {
	public static void main(String[] args) {
		SpringApplication.run(MySpiderApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Thread.currentThread().join();
	}
}
