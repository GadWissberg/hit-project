package handlers;

import handlers.types.AdjacentHandler;
import handlers.types.RoutesHandler;
import handlers.types.ShortestPathsHandler;
import handlers.types.SubmarinesHandler;
import lombok.Getter;

@Getter
public enum HandlersMapping {
	TASK_1(new AdjacentHandler()),
	TASK_2(new RoutesHandler()),
	TASK_3(new ShortestPathsHandler()),
	TASK_4(new SubmarinesHandler());

	private final Handler handler;

	HandlersMapping(final Handler handler) {
		this.handler = handler;
	}
}
