package uk.co.keithsjohnson.designpatterns.command;

public final class CommandMain {
	public static void main(String[] args) {
		CommandFactory cf = CommandFactory.init();
		cf.executeCommand("Light on");
		cf.listCommands();
	}
}