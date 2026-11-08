import java.util.ArrayList;
import java.util.List;

// Nguyen van lop CommandProcessor trong Solution 16 cua tac gia
class CommandProcessor {
	private final List<UndoableCommand> aCommands = new ArrayList<>();

	public void consume(UndoableCommand pCommand) {
		pCommand.execute();
		aCommands.add(pCommand);
	}

	public void undoLast() {
		assert !aCommands.isEmpty();
		UndoableCommand command = aCommands.removeLast();
		command.undo();
	}
}
