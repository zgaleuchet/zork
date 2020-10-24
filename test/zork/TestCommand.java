package zork;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

class TestCommand {

	// Attribut
	Command command;

	@BeforeEach
	void setUp() throws Exception {
		this.command = new Command("go", "south");
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void testCommand() {
		String word1, word2;
		boolean result;
		word1 = command.getCommandWord();
		result = word1.equals("go");
		assertTrue(result);
		//assertEquals(word1, "go");
	}

	@Test
	void testGetCommandWord() {
		fail("Not yet implemented");
	}

	@Test
	void testGetSecondWord() {
		fail("Not yet implemented");
	}

	@Test
	void testIsUnknown() {
		fail("Not yet implemented");
	}

	@Test
	void testHasSecondWord() {
		fail("Not yet implemented");
	}

}
