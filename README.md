# Recipe Application

SimpleDish Database provides simple cooking recipes. It stores dish recipes and their ingredients. 
Each dish recipe needs to have less than or equal to 5 ingredients. The purpose is to help users easily find and manage simple dish recipes.

---

### Database technologies and features:

- App database created without Room, using SQL to create the tables
- Used SQLiteDatabase API with SQL queries to read, update, delete from the database
- Two many to many relationships
- 3rd normal form for tables
- Search database by ingredient or recipe name
- Login functionality made with users table
- Users can bookmark recipes to the database
- Refinement query for clicking a dish to find out it’s ingredients

### Other technologies and features:

- Navigation component
- Repository, Dependency injection for ViewModel
- Multiple ViewModels for each screen, StateFlow/Flow, Coroutines
- MVVM, separation of concerns
- Kotlin / Jetpack Compose

ER diagram of the app database:

![ER diagram](https://github.com/Kealeyl/RecipeApp/blob/main/ERDiagram.png)
