package uk.co.keithsjohnson.designpatterns.command;

/**
 * The Command functional interface.<br/>
 */
@FunctionalInterface
public interface Command {
	public void apply();
}
