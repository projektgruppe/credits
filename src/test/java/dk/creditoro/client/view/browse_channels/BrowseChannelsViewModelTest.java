package dk.creditoro.client.view.browse_channels;

import static org.junit.jupiter.api.Assertions.*;

import java.util.concurrent.CountDownLatch;
import javax.swing.SwingUtilities;

import org.junit.jupiter.api.*;

import dk.creditoro.client.core.ClientFactory;
import dk.creditoro.client.core.ModelFactory;
import dk.creditoro.client.core.ViewModelFactory;
import javafx.embed.swing.JFXPanel;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;

class BrowseChannelsViewModelTest {
	BrowseChannelsViewModel browseChannelsViewModel;
	BrowseChannelsViewModel browseChannelsViewModel2;

	public BrowseChannelsViewModelTest(){
		final CountDownLatch latch = new CountDownLatch(1);
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new JFXPanel(); // initializes JavaFX environment
				latch.countDown();
			}
		});
		try{
			latch.await();
		} catch(InterruptedException e){
		}
		//Makes the client that is used to connect to the internet
		browseChannelsViewModel = new ViewModelFactory(new ModelFactory(new ClientFactory())).getBrowseChannelsViewModel();
	}


	@Test 
	void listPropertyProperty(){
		// First test 1
		//
		assertDoesNotThrow(()-> browseChannelsViewModel.queryParamProperty().setValue("TV") );
		assertDoesNotThrow(()-> browseChannelsViewModel.search() );
		while(browseChannelsViewModel.listPropertyProperty().isEmpty()){
			//Do nothing
		}
		assertTrue(browseChannelsViewModel.listPropertyProperty().get(0).getName().contains("TV"),
					"This should return TV in the name");
		
		// Second test 2
		//
		//
		var randomChOpt = browseChannelsViewModel.listPropertyProperty().stream().findFirst();		
		assertTrue(randomChOpt.isPresent(), 
				"Maybe it could not get any channels from the api?");
		var randCh = randomChOpt.get();
		Button btn = new Button();
		btn.setId(randCh.getIdentifier());
		assertEquals(randCh.getName(), browseChannelsViewModel.channelName(btn),
				"This should always be the same ");

		btn.setId("10");
		assertEquals("", browseChannelsViewModel.channelName(btn), 
				"there should not be a identifier with 10, and this should always be nothing");

		// Third Test 3
		//
		//
		assertNotNull(browseChannelsViewModel.queryParamProperty());
		var randomChOpt2 = browseChannelsViewModel.listPropertyProperty().stream().findFirst();		
		assertTrue(randomChOpt2.isPresent(), 
				"Maybe it could not get any channels from the api?");
		var randCh2 = randomChOpt2.get();
		Button btn3 = new Button();
		btn3.setId(randCh2.getIdentifier());
		Button btn4 = new Button();
		btn4.setId("00");
		var tilePane = new TilePane();
		tilePane.getChildren().addAll(btn3, btn4);

		// CHeck it is ind the right odre
		assertEquals("00", 
				browseChannelsViewModel.sortedChannelList(tilePane).get(0).getId(), 
				"This should always be the first ordre");
		assertEquals(randCh.getIdentifier(), 
				browseChannelsViewModel.sortedChannelList(tilePane).get(1).getId(), 
				"This should always be the second ordre");
		
		// Test 4
		//
		//
		// These are the buttons that can be pressed
		Button btnA = new Button("A");
		Button btnB = new Button("B");
		Button btnC = new Button("C");
		Button btnT = new Button("T");
		HBox alphabet = new HBox();
		alphabet.getChildren().addAll(btnA, btnB, btnC, btnT);
		// Thise create a push on the button
		// Action event 
		ActionEvent ae = new ActionEvent(btnT, ActionEvent.NULL_SOURCE_TARGET);

		// var randomCh = browseChannelsViewModel.listPropertyProperty().get().get(0);

		// This is the list we want sorted
		Button btn5 = new Button();
		btn5.setId("11");
		Button btn6 = new Button();
		btn6.setId(randCh.getIdentifier());
		var tilePane1 = new TilePane();
		System.out.println("Channel name that is search after: " + randCh.getName());
		tilePane1.getChildren().addAll(btn6, btn5);
		
		var sortedList = browseChannelsViewModel.sortedByCharacter(tilePane1.getChildren(), ae, alphabet);
		System.out.println("size: "+sortedList.size() + "\n\n\n\n");
		assertEquals(randCh.getIdentifier(), sortedList.get(0).getId(), 
				"This should always be sorted first");

	}
}

