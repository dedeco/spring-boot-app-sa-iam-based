#  Spring boot app connecting to AlloyDb 
Spring boot app with JDBC SA mitigating Short lived credentials - IAM based

## Install Java 17
See https://sdkman.io/usage

## If you connect from external for a private ip database cluster.
See https://cloud.google.com/alloydb/docs/connect-external

For this sample, I had set up an intermediary virtual machine (VM) to connect to my cluster, as follows:
1. Create a sample-vm
2. Using SSH install the alloydb proxy. [Here more details](https://cloud.google.com/alloydb/docs/auth-proxy/connect).
3. Run the proxy inside the VM:
```bash
./alloydb-auth-proxy \
"projects/<PROJECT-ID>/locations/us-central1/clusters/pagilacls/instances/pagila"
```
4. At the local machine create a tunnel. [Here more details](https://cloud.google.com/iap/docs/using-tcp-forwarding).
```bash
gcloud compute ssh vm-sample \
    --tunnel-through-iap \
    --zone=us-central1-b \
    --ssh-flag="-L 5432:localhost:5432"
```
5. Set your variables on CloudSaApplication.java:
```java
@Bean
DataSource getDataSource() {
    
        HikariConfig config = new HikariConfig();

        config.setJdbcUrl("jdbc:postgresql://127.0.0.1:5432/pagila");
        config.setUsername("andresousa@google.com");
        config.addDataSourceProperty("ssl", "true");
        config.addDataSourceProperty("sslmode", "disable");
        
        return new CloudSqlAutoIamAuthnDataSource(config);
}
```
5. Using another terminal, run:
```bash
./mvnw spring-boot:run
```
Output:
```
(...)
023-09-12 16:30:41.798  INFO 3023838 --- [  restartedMain] o.s.b.d.a.OptionalLiveReloadServer       : LiveReload server is running on port 35729
2023-09-12 16:30:41.838  INFO 3023838 --- [  restartedMain] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat started on port(s): 8080 (http) with context path ''
2023-09-12 16:30:41.854  INFO 3023838 --- [  restartedMain] b.c.x.cloudSA.CloudSaApplication         : Started CloudSaApplication in 5.913 seconds (JVM running for 6.45)

```

Credits: Thanks Eno Compton for support me




