

import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.apex.RoutingContext;

public class ServiceHandler implements Handler<RoutingContext> {

	
	@Override
	public void handle(RoutingContext routingContext) {
		JsonObject user = new JsonObject();
		user.put("firstname", "Phil");
		user.put("Age", 10);
		
		routingContext.response().end(user.encodePrettily());
		
	
	}

}