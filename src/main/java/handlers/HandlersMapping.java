package handlers;

import handlers.routes.RoutesHandler;
import lombok.Getter;

@Getter
public enum HandlersMapping {
	TASK_1(new MatrixIHandler()),
	TASK_2(new RoutesHandler());

	private final IHandler handler;

	HandlersMapping(final IHandler handler) {
		this.handler = handler;
	}
}
