package kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Date;
import java.util.Properties;
import java.util.Random;


// mvn clean compile assembly:single
// kafka-topics.sh --zookeeper sandbox.hortonworks.com:2181 --list
// if the topic "page_visits" does not exists:
// kafka-topics.sh --zookeeper sandbox.hortonworks.com:2181 --create --topic page_visits --replication-factor 1 --partition 5
// kafka-console-consumer.sh --zookeeper sandbox.hortonworks.com:2181 --topic page_visits --from-beginning
// to run 100 instances - java -cp target/BDHSCode-jar-with-dependencies.jar LogGen 100
// to run infinite instances with 100ms sleep - java -cp target/BDHSCode-jar-with-dependencies.jar LogGen 0 100
public class LogGen {
    private static final String TOPIC = "page_visits";
    private static final String DOMAIN = "www.example.com";

    private static Random rnd = new Random();
    private static final String[] pages = {"about-us",
            "product/1",
            "product/2",
            "product/2",
            "product/3",
            "product/3",
            "product/3",
            "product/3"};

    public static void main(String[] args) {
        long events = Long.parseLong(args[0]);

        Properties props = new Properties();
        props.put("bootstrap.servers", "sandbox.hortonworks.com:6667");
        props.put("metadata.broker.list", "sandbox.hortonworks.com:6667");
        props.put("acks", "1");
        props.put("retries", 0);
        props.put("batch.size", 16384);
        props.put("linger.ms", 1);
        props.put("buffer.memory", 33554432);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("advertised.host.name", "sandbox.hortonworks.com");
        props.put("serializer.class", "kafka.serializer.StringEncoder");
        props.put("partitioner.class", "SimplePartitioner");
        props.put("request.required.acks", "1");


        Producer<String, String> producer = new KafkaProducer<String, String>(props);
        if (events == 0L) {
            long sleep = Long.parseLong(args[1]);
            long i = 0L;
            while(true) {
                producer.send(getMessage());
                System.out.println("sent message " + ++i);
                try {
                    Thread.sleep(sleep);
                } catch (InterruptedException e) {
                    producer.close();
                    e.printStackTrace();
                    System.exit(1);
                }
            }
        } else {
            for (long nEvents = 0; nEvents < events; nEvents++) {
                producer.send(getMessage());
                System.out.println("sent message " + (nEvents + 1) + " from " + events);
            }
            producer.close();
        }
    }

    private static ProducerRecord<String, String> getMessage() {
        long runtime = new Date().getTime();
        int lastIpDigit = 49;
        if(rnd.nextBoolean()) {
            lastIpDigit = rnd.nextInt(255);
        }
        String ip = "192.168.2." + lastIpDigit;
        String msg = runtime + "," + DOMAIN + "/" + pages[rnd.nextInt(pages.length)] + "," + ip;
        ProducerRecord<String, String> data = new ProducerRecord<String, String>(TOPIC, ip, msg);
        return data;
    }
}
