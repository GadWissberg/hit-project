package handlers;

import handlers.first.AdjacentHandler;
import handlers.second.RoutesHandler;
import handlers.third.ShortestPathsHandler;
import lombok.Getter;

@Getter
public enum HandlersMapping {
	TASK_1(new AdjacentHandler()),
	TASK_2(new RoutesHandler()),
	TASK_3(new ShortestPathsHandler());

	private final Handler handler;

	HandlersMapping(final Handler handler) {
		this.handler = handler;
	}
}
