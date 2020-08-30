package handlers;

import handlers.types.first.AdjacentHandler;
import handlers.types.fourth.SubmarinesHandler;
import handlers.types.second.RoutesHandler;
import handlers.types.third.ShortestPathsHandler;
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
