import java.util.EnumMap;
import java.util.Objects;

// Interface Show và Movie từ Exercise 1
interface Show {
	String description();
	int runningTime();
	Show copy();
}

class Movie implements Show {
	private String aTitle;
	private int aYear;
	private int aTime;

	public Movie(String pTitle, int pYear, int pTime) {
		aTitle = pTitle;
		aYear = pYear;
		aTime = pTime;
	}

	public String description() {
		return aTitle + " (" + aYear + ")";
	}

	public int runningTime() {
		return aTime;
	}

	public Show copy() {
		return new Movie(aTitle, aYear, aTime);
	}
}

interface Command {
	void execute();
}

interface UndoableCommand extends Command {
	void undo();
}

class Program3 {
	public enum Day {
		MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY
	}

	private static final Show NULL_SHOW = createNullShow();

	private static Show createNullShow() {
		return new Show() {
			public String description() { return ""; }
			public int runningTime() { return 0; }
			public Show copy() { return createNullShow(); }
			public int hashCode() { return 0; }
			public boolean equals(Object pObject) {
				return pObject != null && pObject.getClass() == this.getClass();
			}
		};
	}

	private final EnumMap<Day, Show> aShows = new EnumMap<>(Day.class);

	public Program3() {
		clear();
	}

	public UndoableCommand createAddCommand(Show pShow, Day pDay) {
		return new UndoableCommand() {
			public void execute() {
				add(pShow, pDay);
			}

			public void undo() {
				remove(pDay);
			}
		};
	}

	public UndoableCommand createRemoveCommand(Day pDay) {
		return new UndoableCommand() {
			Show show = aShows.get(pDay);

			public void execute() {
				show = aShows.get(pDay);
				remove(pDay);
			}

			public void undo() {
				add(show, pDay);
			}
		};
	}

	public UndoableCommand createClearCommand() {
		return new UndoableCommand() {
			EnumMap<Day, Show> previousShows = new EnumMap<>(aShows);

			public void execute() {
				previousShows = new EnumMap<>(aShows);
				clear();
			}

			public void undo() {
				aShows.putAll(previousShows);
			}
		};
	}

	private void clear() {
		for (Day day : Day.values()) {
			aShows.put(day, NULL_SHOW);
		}
	}

	private void add(Show pShow, Day pDay) {
		assert pShow != null && pDay != null;
		aShows.put(pDay, pShow);
	}

	private void remove(Day pDay) {
		assert pDay != null;
		aShows.put(pDay, NULL_SHOW);
	}

	public Show get(Day pDay) {
		assert pDay != null;
		return aShows.get(pDay);
	}

	public String toString() {
		StringBuilder result = new StringBuilder();
		for (Day day : aShows.keySet()) {
			if (aShows.containsKey(day)) {
				result
					.append(String.format("%9s", day.name()))
					.append(": ")
					.append(aShows.get(day).description())
					.append("\n");
			}
		}
		return result.toString();
	}
}

public class Exercise15 {
	public static void main(String[] args) {
		Program3 program = new Program3();
		UndoableCommand command = program.createAddCommand(new Movie("Movie 1", 2000, 120), Program3.Day.MONDAY);
		command.execute();
		System.out.println("Sau execute Add Movie 1:\n" + program);
		command.undo();
		System.out.println("Sau undo Add Movie 1:\n" + program);
		command.execute();
		command = program.createAddCommand(new Movie("Movie 2", 2010, 90), Program3.Day.WEDNESDAY);
		command.execute(); // No undo on purpose
		System.out.println("Sau khi them Movie 2:\n" + program);
		command = program.createRemoveCommand(Program3.Day.MONDAY);
		command.execute();
		System.out.println("Sau khi remove MONDAY:\n" + program);
		command.undo();
		System.out.println("Sau khi undo remove MONDAY:\n" + program);
		command = program.createClearCommand();
		command.execute();
		System.out.println("Sau khi clear:\n" + program);
		command.undo();
		System.out.println("Sau khi undo clear:\n" + program);
	}
}