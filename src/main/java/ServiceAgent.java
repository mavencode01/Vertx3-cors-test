

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.apex.Router;
import io.vertx.ext.apex.handler.AuthHandler;
import io.vertx.ext.apex.handler.BasicAuthHandler;
import io.vertx.ext.apex.handler.BodyHandler;
import io.vertx.ext.apex.handler.CookieHandler;
import io.vertx.ext.apex.handler.CorsHandler;
import io.vertx.ext.apex.handler.SessionHandler;
import io.vertx.ext.apex.handler.StaticHandler;
import io.vertx.ext.apex.sstore.LocalSessionStore;
import io.vertx.ext.auth.AuthProvider;
import io.vertx.ext.auth.AuthService;
import io.vertx.ext.auth.shiro.impl.PropertiesAuthRealm;
import io.vertx.ext.auth.shiro.impl.ShiroAuthProviderImpl;

public class ServiceAgent extends AbstractVerticle {
	
	private static final String SESSION = "login_session";
	private static final String BASIC_AUTH_REALM = "MyRealm";
	private AuthProvider authProvider;
     
	
	@Override
	public void start() throws Exception {
		super.start();
		
		JsonObject config = new JsonObject();
		authProvider =  new ShiroAuthProviderImpl(vertx, new PropertiesAuthRealm(config));
		
		AuthService authService = AuthService.create(vertx, authProvider);
		authService.start();
		
		AuthHandler basicAuthHandler = BasicAuthHandler.create(authProvider, BASIC_AUTH_REALM);
		
		Router router = Router.router(vertx);		
		router.route().handler(CookieHandler.create());
		router.route().handler(SessionHandler.create(LocalSessionStore.create(vertx, SESSION, 10000)));
		router.route().handler(basicAuthHandler);
		router.route().handler(BodyHandler.create());
		
		//CorsHandler corsHandler = CorsHandler.create("http://localhost:8081");
		CorsHandler corsHandler = CorsHandler.create("*");
		corsHandler.allowedMethod(HttpMethod.GET);
		corsHandler.allowedMethod(HttpMethod.POST);
		corsHandler.allowedMethod(HttpMethod.PUT);
		corsHandler.allowedMethod(HttpMethod.DELETE);
		corsHandler.allowedHeader("Authorization");
		corsHandler.allowedHeader("Content-Type");
		corsHandler.allowedHeader("Access-Control-Allow-Origin");
		corsHandler.allowedHeader("Access-Control-Allow-Headers");		
		
		router.route().handler(corsHandler);
		
		router.post("/api/login").handler(new ServiceHandler()).consumes("application/json").produces("application/json");		
		
		
		vertx.createHttpServer().requestHandler(router::accept).listen(80, action -> {
			if (action.succeeded()){
				System.out.println("Server running...");
			}else{
				System.out.println("Unable to start server. Reason - {}" + action.cause().getMessage());
			}
		});		
		
		Router siteRouter = Router.router(vertx);
		
		siteRouter.route().handler(StaticHandler.create().setWebRoot("webroot/admin"));
		
		vertx.createHttpServer().requestHandler(siteRouter::accept).listen(8081, action -> {
			if (action.succeeded()){
				System.out.println("Web site loaded...");
			}else{
				System.out.println("Unable to start web site. Reason - " + action.cause().getMessage());
			}
		});	
	}
}
