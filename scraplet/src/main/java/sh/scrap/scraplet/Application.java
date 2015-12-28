package sh.scrap.scraplet;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.InstanceProfileCredentialsProvider;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import org.apache.ignite.Ignite;
import org.apache.ignite.Ignition;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.spi.discovery.tcp.TcpDiscoverySpi;
import org.apache.ignite.spi.discovery.tcp.ipfinder.TcpDiscoveryIpFinder;
import org.apache.ignite.spi.discovery.tcp.ipfinder.multicast.TcpDiscoveryMulticastIpFinder;
import org.apache.ignite.spi.discovery.tcp.ipfinder.s3.TcpDiscoveryS3IpFinder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

@SpringBootApplication
public class Application {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(Application.class, args);
    }

    @Bean Ignite ignite(IgniteConfiguration config) {
        return Ignition.start(config);
    }

    @Bean IgniteConfiguration config(TcpDiscoveryIpFinder ipFinder) {
        IgniteConfiguration config = new IgniteConfiguration();
        TcpDiscoverySpi tcpDiscoverySpi = new TcpDiscoverySpi();
        tcpDiscoverySpi.setIpFinder(ipFinder);
        config.setDiscoverySpi(tcpDiscoverySpi);
        return config;
    }

    @Bean @Profile("development")
    TcpDiscoveryIpFinder multicastIpFinder() {
        return new TcpDiscoveryMulticastIpFinder();
    }

    @Bean @Profile("production")
    TcpDiscoveryIpFinder awsIpFinder() {
        AWSCredentialsProvider provider = new InstanceProfileCredentialsProvider();
        TcpDiscoveryS3IpFinder s3IpFinder = new TcpDiscoveryS3IpFinder();
        s3IpFinder.setAwsCredentials(provider.getCredentials());
        s3IpFinder.setBucketName("scrap.sh-cluster");
        return s3IpFinder;
    }

    @Bean AmazonDynamoDB dynamoDB() {
        return new AmazonDynamoDBClient();
    }

}
