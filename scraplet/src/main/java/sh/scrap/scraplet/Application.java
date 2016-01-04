package sh.scrap.scraplet;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.InstanceProfileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.document.Table;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import org.apache.ignite.cache.CacheAtomicityMode;
import org.apache.ignite.cache.CacheMode;
import org.apache.ignite.configuration.CacheConfiguration;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.spi.discovery.tcp.TcpDiscoverySpi;
import org.apache.ignite.spi.discovery.tcp.ipfinder.TcpDiscoveryIpFinder;
import org.apache.ignite.spi.discovery.tcp.ipfinder.multicast.TcpDiscoveryMulticastIpFinder;
import org.apache.ignite.spi.discovery.tcp.ipfinder.s3.TcpDiscoveryS3IpFinder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import sh.scrap.scraplet.store.DataScrapperCacheKey;
import sh.scrap.scraplet.store.DataScrapperCacheStore;
import sh.scrap.scrapper.DataScrapperBuilder;

import java.net.UnknownHostException;

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
    TcpDiscoveryIpFinder awsIpFinder(
            @Value("${aws.accessKeyId}") String accessKeyId,
            @Value("${aws.secretAccessKey}") String secretAccessKey)
            throws UnknownHostException {
        AWSCredentialsProvider provider = new InstanceProfileCredentialsProvider(false);
        TcpDiscoveryS3IpFinder s3IpFinder = new TcpDiscoveryS3IpFinder();
        s3IpFinder.setAwsCredentials(new BasicAWSCredentials(accessKeyId, secretAccessKey));
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

    @Bean IgniteCache<DataScrapperCacheKey, DataScrapperBuilder> builderCache(Ignite ignite,
            CacheConfiguration<DataScrapperCacheKey, DataScrapperBuilder> builderCacheConfig) {
        return ignite.getOrCreateCache(builderCacheConfig);
    }

    @Bean CacheConfiguration<DataScrapperCacheKey, DataScrapperBuilder> builderCacheConfig() {
        CacheConfiguration<DataScrapperCacheKey, DataScrapperBuilder> config = new CacheConfiguration<>();
        config.setName("scraplet");
        config.setAtomicityMode(CacheAtomicityMode.TRANSACTIONAL);
        config.setReadThrough(true);
        config.setCacheStoreFactory(DataScrapperCacheStore::new);
        config.setCacheMode(CacheMode.PARTITIONED);
        config.setBackups(1);
        return config;
    }
}
