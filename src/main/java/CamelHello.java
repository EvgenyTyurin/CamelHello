import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.activemq.ActiveMQComponent;
import org.apache.camel.impl.DefaultCamelContext;

/**
 * Camel Hello World demo
 * Route message from ActiveMQ queue to console using Camel
 * Sep 2019 EvgenyT
 */

public class CamelHello {
    public static void main(String[] args) {
        // Create camel context
        CamelContext context = new DefaultCamelContext();
        try {
            // Add ActiveMQ JMS component to context
            context.addComponent("activemq",
                    ActiveMQComponent.activeMQComponent("vm://localhost?broker.persistent=false"));
            // Message routing configure here
            context.addRoutes(new RouteBuilder() {
                @Override
                public void configure() throws Exception {
                    // Add message routing from ActiveMQ queue to console
                    from("activemq:queue:test.queue")
                            .to("stream:out");                }
            });
            // Start camel context
            context.start();
            // Send hello to queue
            ProducerTemplate template = context.createProducerTemplate();
            template.sendBody("activemq:test.queue", "Hello World");
            // Wait 2 secs for message routing
            Thread.sleep(2000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            context.stop();
        }
    }
}
