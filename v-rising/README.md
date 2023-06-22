## Build

Clone the project:

```
git clone git@github.com:DarkAtra/bfme2-modding-utils.git
git switch chore/v-rising-save-edits
```

Ensure you have properly installed JDK 17 and Maven 3.x. Then cd into the project root and build it using the following command:

```
mvn clean install
```

## Usage

Assuming that we have the v rising save file `v-rising.save` in the current directory and want to replace the steam-id `76561198032610434`
with `76561197999039092`, we can run the following command:

```
cd v-rising
java -jar ./target/v-rising-jar-with-dependencies.jar v-rising.save 76561198032610434 76561197999039092
```

Your output should look like this:

```
Looking for 76561198032610434 in v-rising.save
Found Steam ID at 27962985: 82 e4 4f 04 01 00 10 01
Changing Steam ID to 76561197999039092 and writing new save file to '<...>\bfme2-modding-utils\v-rising\new.save'
```

We can then validate that our change was successful by running the following command:

```
java -jar ./target/v-rising-jar-with-dependencies.jar new.save 76561197999039092
```

This should output:

```
Looking for 76561197999039092 in new.save
Found Steam ID at 23882365: 74 a2 4f 02 01 00 10 01
Not writing new save file because the you ran this tool in read-only mode.
```
