package sh.scrap.scraplet;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.InstanceProfileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.util.EC2MetadataUtils;
import org.apache.ignite.Ignite;
import org.apache.ignite.Ignition;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.spi.communication.tcp.TcpCommunicationSpi;
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

    @Bean IgniteConfiguration config(TcpDiscoveryIpFinder ipFinder, TcpCommunicationSpi communicationSpi) {
        IgniteConfiguration config = new IgniteConfiguration();
        TcpDiscoverySpi tcpDiscoverySpi = new TcpDiscoverySpi();
        tcpDiscoverySpi.setIpFinder(ipFinder);
        config.setDiscoverySpi(tcpDiscoverySpi);
        config.setCommunicationSpi(communicationSpi);
        return config;
    }

    @Bean @Profile("development")
    TcpDiscoveryIpFinder multicastIpFinder() {
        return new TcpDiscoveryMulticastIpFinder();
    }

    @Bean @Profile("production")
    TcpDiscoveryIpFinder awsIpFinder() {
        AWSCredentialsProvider provider = new InstanceProfileCredentialsProvider(false);
        TcpDiscoveryS3IpFinder s3IpFinder = new TcpDiscoveryS3IpFinder();
        AWSCredentials credentials = provider.getCredentials();
        credentials = new BasicAWSCredentials(
                credentials.getAWSAccessKeyId(),
                credentials.getAWSSecretKey());
        s3IpFinder.setAwsCredentials(credentials);
        s3IpFinder.setBucketName("scrap.sh-cluster");
        return s3IpFinder;
    }

    @Bean AmazonDynamoDB dynamoDB() {
        AmazonDynamoDBClient client = new AmazonDynamoDBClient();
        client.setRegion(Region.getRegion(Regions.US_WEST_2));
        return client;
    }

    @Bean Table scrapletTable(AmazonDynamoDB dynamoDB) {
        return new Table(dynamoDB, "scraplet");
    }

    @Bean @Profile("development")
    TcpCommunicationSpi localTcpCommunication() {
        return new TcpCommunicationSpi();
    }

    @Bean @Profile("production")
    TcpCommunicationSpi awsTcpCommunication() {
        TcpCommunicationSpi communicationSpi = new TcpCommunicationSpi();
        String ipAddresss = EC2MetadataUtils.getPrivateIpAddress();
        communicationSpi.setLocalAddress(ipAddresss);
        return communicationSpi;
    }
}
