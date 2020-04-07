# scamazon
BUGS:
Feel free to write any bugs or thoughts you come across here.


OUTLINE:

-Asks a user for their username and password. Along with an option to create a new user
	-if the user doesn't exist, inform that the user is not found
	-if the user exists but the password is wrong. Inform the user
	-NOTE: I'm not too worried about sanitization of input since this isn't a public thing

-IF THE USER IS NOT THE ADMIN
	-PRINT OUT the categories that are available to buy from to the user
		-if user enters a non-existent category, inform the user and ask again
	-once the user chooses a category, print out the avaialable products in that category
		-if that category has nothing in it, inform the user and back out to the category select
		-PRINT OUT itemName, price, stock, and description
			-if the item has >= 5 in stock, just print in stock
			-if < 5, print how many are left
			-if out of stock, print out of stock for that item
	-the user can choose multiple items to add to a 'cart'
		-if the user tries to add something that doesn't exist, inform the user
		-would probably be easier to make a limited size cart as opposed to unlimited
		-everytime something is added, let the user know how many items are in the cart
	-once the user is done selecting items to purchase, go to a checkout process
		-ask the user if they want the items shipped to their current stored address
			-if yes, continue
			-if no, ask the user for the new information and modify their address info appropriately
		-if the order goes through correctly, inform the user, modify the appropriate database fields, and back out to categories

-IF THE USER IS THE ADMIN
	-give the option to edit categories, items, emp, or users
		-if the admin chooses something else, inform them and ask again
	-once the admin chooses one, ask if they want to add, delete, or update the specified table
		-emps and users cannot be deleted, if the admin tries to delete from these, inform them and ask again
	-if adding something to a table
		-just ask for required fields, if the admin doesn't enter correctly, inform them and ask again
	-if updating a table
		-ask the admin for the appropriate primary key for the row they want updated
			-if that row doesn't exist, inform the admin and give the option to try again or back out to the edit selection
		-ask what field they would like to update
			-if that field doesn't exist, inform the admin and ask again
		-once the field is selected, display the current info for that field and ask for the updated info
			-if info doesn't match field type, inform the admin and ask again
		-display the current info and updated info side-by-side and ask the admin if they are sure they want to make the change
			-if yes, update and back out to edit selection
			-if no, don't update and ask if the admin would like to continue update the specified field, if not, back out