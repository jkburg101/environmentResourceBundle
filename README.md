# environmentResourceBundle
A Java ResourceBundle utility, allowing properties to be replaced by environment/system variables at runtime.

ResourceBundles in Java are able to pull from .properties files in order to display localized text. However, sometimes it may be useful
to allow certain properties or substrings of properties to come from the application's environment.

Say you had a file called userInterface.properties with the following property inside:
```
welcome.text=Hello there! Welcome to localhost:8080
```

Then, in your java code, you've done something like this:
```java
public static void main(String[] args) {
  ResourceBundle userInterfaceBundle = ResourceBundle.getBundle("userInterface");
  System.out.println(userInterfaceBundle.getString("welcome.text"));
}
```

This will print out your welcome message, and it looks fantastic! Until you need to deploy this application onto a dev, test, and 
production environment. The environmentResourceBundle library makes this simple and natural, by allowing you to specify variables
inside your properties files.
```
welcome.text=Hello there! Welcome to ${host_server}!
```
Then add one line of code:
```java
@EnvironmentResourceBundle(names={"userInterface"})
public static void main(String[] args) {
  ResourceBundle userInterfaceBundle = ResourceBundle.getBundle("userInterface");
  System.out.println(userInterfaceBundle.getString("welcome.text"));
}
```

## Where does it search for variables
Variables can be pulled from two places: first from Java system properties, then, if that associated property is not found, system
environment variables. If a variable is not found that matches in either of those two places, the property value will remain unchanged
in the application. 

In our example above, if an application was started as such:

```
java -Dhost_server=github.com -jar MyApplication.jar
```
Then, as expected, the output would show as "Hello there! Welcome to github.com!"

## How does it work under the hood? 
By default, resourceBundles first look for .class files that have the specified resourceBundle name, and then .properties files
if no .class file is found. The annotation @EnvironmentResourceBundle will, at compile time, create a .class file in your 
code with the same name as the one provided, essentially overriding the .properties file. That allows it to read through the corresponding
file, and then provide your application with properties having variable substitution. 

//TODO: will add package availability on MavenCentral
