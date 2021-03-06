package com.ociweb.oe.foglight.api;


import com.ociweb.iot.maker.FogApp;
import com.ociweb.iot.maker.FogRuntime;
import com.ociweb.iot.maker.Hardware;
import com.ociweb.pronghorn.network.config.HTTPHeaderDefaults;

public class HTTPServer implements FogApp
{

	int emptyResponseRouteId;
	int smallResponseRouteId;
	int largeResponseRouteId;
	int fileServerId;
	int port;
	
	
	byte[] myArgName = "myarg".getBytes();
	
	public HTTPServer(int port) {
		this.port = port;
	}
	
    @Override
    public void declareConnections(Hardware c) {

        c.useHTTP1xServer(port).setHost("0.0.0.0").useInsecureServer();
		emptyResponseRouteId = c.defineRoute(HTTPHeaderDefaults.COOKIE).path("/testpageA?arg=#{myarg}").routeId();
		smallResponseRouteId = c.defineRoute().path("/testpageB").routeId();
		largeResponseRouteId = c.defineRoute(HTTPHeaderDefaults.COOKIE).path("/testpageC").routeId();
		fileServerId         = c.defineRoute().path("/file${path}").routeId();
		c.enableTelemetry();
		
    }


    @Override
    public void declareBehavior(FogRuntime runtime) {
        runtime.addRestListener(new RestBehaviorEmptyResponse(runtime, myArgName))
                 .includeRoutes(emptyResponseRouteId);
        
        runtime.addRestListener(new RestBehaviorSmallResponse(runtime))
        		.includeRoutes(smallResponseRouteId);
        
        runtime.addRestListener(new RestBehaviorLargeResponse(runtime))
        		 .includeRoutes(largeResponseRouteId);
        
        //NOTE .includeAllRoutes() can be used to write a behavior taking all routes
        
        //NOTE when using the above no routes need to be registered and if they are
        //     all other routes will return a 404

    }
   
}
