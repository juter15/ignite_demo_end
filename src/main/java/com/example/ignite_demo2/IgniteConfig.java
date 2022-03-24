package com.example.ignite_demo2;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteException;
import org.apache.ignite.Ignition;
import org.apache.ignite.cache.CacheAtomicityMode;
import org.apache.ignite.cache.CacheMode;
import org.apache.ignite.cache.CacheWriteSynchronizationMode;
import org.apache.ignite.cluster.ClusterState;
import org.apache.ignite.configuration.*;
import org.apache.ignite.spi.discovery.tcp.TcpDiscoverySpi;
import org.apache.ignite.spi.discovery.tcp.ipfinder.vm.TcpDiscoveryVmIpFinder;
import org.apache.ignite.springdata20.repository.config.EnableIgniteRepositories;
import org.apache.ignite.springdata20.repository.config.RepositoryConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.Arrays;
import java.util.Collections;

@Configuration
@EnableJpaRepositories(basePackages = "com.example.ignite_demo2", excludeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = RepositoryConfig.class))
@EnableIgniteRepositories(basePackages = "com.example.ignite_demo2", includeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = RepositoryConfig.class))
public class IgniteConfig {

    @Bean(name = "igniteCfgInstance")
    public IgniteConfiguration igniteInstance(){
        IgniteConfiguration igniteConfiguration = new IgniteConfiguration();
        //igniteConfiguration.setClientMode(true);
        igniteConfiguration.setPeerClassLoadingEnabled(true);
        igniteConfiguration.setDeploymentMode(DeploymentMode.CONTINUOUS);
//        ConnectorConfiguration connectorConfiguration = new ConnectorConfiguration();
//        connectorConfiguration.setPort(47500);
        igniteConfiguration.setMetricsLogFrequency(60000);
        igniteConfiguration.setQueryThreadPoolSize(2);
        igniteConfiguration.setDataStreamerThreadPoolSize(1);
        igniteConfiguration.setManagementThreadPoolSize(2);
        igniteConfiguration.setRebalanceThreadPoolSize(1);
        igniteConfiguration.setAsyncCallbackPoolSize(2);
        //IgniteLogger log = new Slf();
        //igniteConfiguration.setGridLogger(log);

        // Ignite persistence configuration.
        DataStorageConfiguration storageCfg = new DataStorageConfiguration()
                .setWalMode(WALMode.LOG_ONLY)
                .setStoragePath("C:\\apache-ignite-2.12.0-bin\\work")
                .setWalPath("C:\\apache-ignite-2.12.0-bin\\work" + "/wal")
                .setWalArchivePath("C:\\apache-ignite-2.12.0-bin\\work" + "/wal/archive");

        // Enabling the persistence.
        storageCfg.getDefaultDataRegionConfiguration().setPersistenceEnabled(true);

        // Applying settings.
        igniteConfiguration.setDataStorageConfiguration(storageCfg);

        igniteConfiguration.setIgniteInstanceName("test-2");
//        BinaryConfiguration binaryConfiguration = new BinaryConfiguration();
//        binaryConfiguration.setCompactFooter(false);
//        igniteConfiguration.setBinaryConfiguration(binaryConfiguration);
        TcpDiscoverySpi tcpDiscoverySpi = new TcpDiscoverySpi();
        TcpDiscoveryVmIpFinder tcpDiscoveryVmIpFinder = new TcpDiscoveryVmIpFinder();
        tcpDiscoveryVmIpFinder.setAddresses(Collections.singletonList("127.0.0.1:47500..47509"));
        tcpDiscoverySpi.setIpFinder(tcpDiscoveryVmIpFinder);
        igniteConfiguration.setDiscoverySpi(tcpDiscoverySpi);
        return igniteConfiguration;
    }

    @Bean(destroyMethod = "close", name="igniteInstance")
    public Ignite ignite(IgniteConfiguration igniteConfiguration) throws IgniteException {
        Ignite ignite = Ignition.start(igniteConfiguration);
        ignite.cluster().state(ClusterState.ACTIVE);

        // Cache configuration
        CacheConfiguration<Long, test1> test1 = new CacheConfiguration<Long, test1>("test1")
                .setCacheMode(CacheMode.PARTITIONED)
                //.setBackups(1)
                .setAtomicityMode(CacheAtomicityMode.TRANSACTIONAL)
                .setWriteSynchronizationMode(CacheWriteSynchronizationMode.PRIMARY_SYNC)
                .setIndexedTypes(Long.class, test1.class);

        ignite.getOrCreateCaches(Arrays.asList(test1)); //캐시 생성. 여러개 추가 가능성
        return ignite;
    }
}
