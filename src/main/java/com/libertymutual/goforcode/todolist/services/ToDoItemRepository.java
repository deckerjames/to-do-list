// ToDoItemRepository.java
package com.libertymutual.goforcode.todolist.services;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ser.std.StdKeySerializers.Default;
import com.libertymutual.goforcode.todolist.models.ToDoItem;

@Service
public class ToDoItemRepository {
	ArrayList<ToDoItem> toDoItems;
	private int nextId = 1;
	
	public ToDoItemRepository() {
		toDoItems = new ArrayList<>();
	}

	// /**
	// * Get all the items from the file.
	// * @return A list of the items. If no items exist, returns an empty list.
	// */
	public List<ToDoItem> getAll() {

		if (toDoItems.size() == 0) {

			try (Reader in = new FileReader("list.csv")) {
				for (CSVRecord record : CSVFormat.DEFAULT.parse(in).getRecords()) {
					int id = Integer.parseInt(record.get(0));
					String text = record.get(1);
					boolean isComplete = Boolean.parseBoolean(record.get(2));
					ToDoItem toDoItem = new ToDoItem();
					toDoItem.setId(id);
					toDoItem.setText(text);
					toDoItem.setComplete(isComplete);
					toDoItems.add(toDoItem);
					nextId += 1;
				}

			} catch (FileNotFoundException e) {
				return Collections.emptyList(); 

			} catch (IOException e) {
				
			}
		}
		return toDoItems;
	}

	/**
	 * Assigns a new id to the ToDoItem and saves it to the file.
	 * 
	 * @param item
	 *            The to-do item to save to the file.
	 * @param id
	 * @throws IOException
	 */
	public void create(ToDoItem item) {
		// write to file
		item.setId(nextId);
		nextId += 1;
		toDoItems.add(item);
	}

	/**
	 * Gets a specific ToDoItem by its id.
	 * 
	 * @param id
	 *            The id of the ToDoItem.
	 * @return The ToDoItem with the specified id or null if none is found.
	 */
	public ToDoItem getById(int id) {
		return toDoItems.stream().filter(item -> item.getId() == id).findFirst().orElse(null);
	}

	/**
	 * Updates the given to-do item in the file.
	 * 
	 * @param item
	 *            The item to update.
	 */
	public void update(ToDoItem item) {
		try (FileWriter fileWriter = new FileWriter("list.csv")) {

			CSVPrinter csvFilePrinter = CSVFormat.DEFAULT.print(fileWriter);

			for (ToDoItem i : toDoItems) {

				Object[] record = new String[] { String.valueOf(i.getId()), i.getText(), String.valueOf(i.isComplete()) };
				csvFilePrinter.printRecord(record);

			}

		} catch (IOException e) {
			System.out.println("issue creating item");

		}
	}
}