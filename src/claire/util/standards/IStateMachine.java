package claire.util.standards;

import claire.util.standards.crypto.IState;

public interface IStateMachine<State extends IState<?>> {
	
	State getState();
	void loadState(State state);
	void updateState(State state);
	
	void reset();

}
