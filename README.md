# scamazon
BUGS:
Feel free to write any bugs or thoughts you come across here.


OUTLINE:


-Asks a user for their username and password. Along with an option to create a new user
	-will differentiate between user, employee and admin login and will allow for different features
	-informs user if either the username is incorrect or the password is incorrect
	-use preparedstatements to sanitize input in login phase
	-user is informed if the account ID is already in use when creating account
	-can only be used to create a customer account

-IF THE USER IS NOT THE ADMIN OR AN EMP
	-PRINT OUT the categories that are available to buy from to the user
		-if user enters a non-existent category, inform the user and ask again
	-once the user chooses a category, print out the avaialable products in that category
		-if that category has nothing in it, inform the user and back out to the category select
		-PRINT OUT itemName, price, stock, and description
			-if the item has >= 5 in stock, just print in stock
			-if < 5, print how many are left
			-if out of stock, print out of stock for that item
	-the user can select an item to view the reviews of the item
		-informs user if there are no reviews for that item
	-the user can choose multiple items to add to a 'cart'
		-if the user tries to add something that doesn't exist, inform the user
		-if the user tries to order more units that are in stock inform the user and do not add to cart
		-cart stores product information and the quantity ordered
		-everytime something is added, let the user know how many items are in the cart
	-the user can check the items, price and quantity ordered of each item in their cart prior to checkout
	-once the user is done selecting items to purchase, go to a checkout process
		-ask the user if they want the items shipped to their current stored address
			-if yes, continue
			-if no, ask the user for the new information and modify their address info appropriately
		-if the order goes through correctly, inform the user, modify the appropriate database fields, and back out to categories
		-lower supply of items based on the quantity ordered
		-empty cart after successful order
	-give the option to post a review in the main menu selection
		-list the items the user has ordered and ask which the user would like to review
			-if the user has not ordered any anything, inform the user and back out to categories
			-lists in order of itemID and will only display one per distinct item
		-when the user chooses one of the options, take input for the review
		-take an int for rating
		-add the review to reviews
		-modify the avgRating in reviews by taking the average amount of ratings
		-if rating is null set to initial rating score
		-the user can modify their review if they had already placed a review for that certain product
		-users cannot place multiple reviews for any given product
	-user can choose to change their password
		-will check to see if both password inputs are the same and update database accordingly
	-empty cart upon logout
		

-IF THE USER IS AN EMP
	-give the option of which category to modify, since an employee may manage multiple categories
	-once the emp choose a category, ask if they want to add, delete, or update the specified table
	-if adding something to a table
		-just ask for required fields, if the emp doesn't enter correctly, inform them and ask again
	-if updating a table
		-ask the emp for primary key for what they want to update
			-if that row doesn't exist, inform the emp and give the option to try again or back out to edit selection
		-ask what field they would like to update
			-if that field doesn't exist, infomr the emp and ask again
		-once the field is selected, display current info and ask for updated info
		-display info side-by-side and ask the emp again if they want to make the update

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
				-the employee managing that field will be notified of the update
			-if no, don't update and ask if the admin would like to continue update the specified field, if not, back out
