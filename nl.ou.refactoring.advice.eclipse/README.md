# Configuration

## Maven Settings.xml Configuration for GitHub Packages

This `settings.xml` file is configured to work with GitHub Packages for the Open Universiteit Refactoring Diagnosis project.

### How to Use

#### 1. Place the File

Copy or move this file to your Maven settings directory:

- **Windows**: `C:\Users\YourUsername\.m2\settings.xml`
- **Mac/Linux**: `~/.m2/settings.xml`

#### 2. Set Environment Variables

Before using Maven, set these environment variables:

```bash
# Windows (Command Prompt)
set GITHUB_USERNAME=your_github_username
set GITHUB_TOKEN=your_personal_access_token

# Windows (PowerShell)
$env:GITHUB_USERNAME="your_github_username"
$env:GITHUB_TOKEN="your_personal_access_token"

# Mac/Linux (Bash)
export GITHUB_USERNAME=your_github_username
export GITHUB_TOKEN=your_personal_access_token
```

#### 3. Create a Personal Access Token

1. Go to GitHub → Settings → Developer settings → Personal access tokens
2. Click "Generate new token"
3. Give it a descriptive name (e.g., "Maven Deployment")
4. Select these scopes:
   - `read:packages`
   - `write:packages`
5. Click "Generate token"
6. **Copy the token immediately** (you won't see it again)

#### 4. Using the Configuration

Now you can use Maven commands normally:

```bash
# Build the project
mvn clean install

# Deploy to GitHub Packages
mvn deploy

# The GitHub profile is activated by default
```

### Configuration Details

#### Servers Configuration
```xml
<servers>
  <server>
    <id>github</id>
    <username>${env.GITHUB_USERNAME}</username>
    <password>${env.GITHUB_TOKEN}</password>
  </server>
</servers>
```

- Uses environment variables for security
- The `github` ID matches the repository ID in `pom.xml`

#### Profile Configuration
```xml
<profile>
  <id>github</id>
  <activation>
    <activeByDefault>true</activeByDefault>
  </activation>
  <repositories>
    <repository>
      <id>github</id>
      <name>GitHub Packages</name>
      <url>https://maven.pkg.github.com/Open-Universiteit-Refactoring-Diagnosis/GraphAdvice</url>
    </repository>
  </repositories>
</profile>
```

- Activated by default
- Points to the GitHub Packages repository
- Falls back to Maven Central for other dependencies

### Security Best Practices

1. **Never commit tokens** to your repository
2. **Use fine-grained tokens** with minimal permissions
3. **Rotate tokens regularly** (every 30-90 days)
4. **Use environment variables** instead of hardcoding credentials
5. **Revoke unused tokens** immediately

### Troubleshooting

#### "Could not find artifact" Errors

1. Verify the token has the correct permissions
2. Check that the artifact exists in GitHub Packages
3. Try forcing an update:
   ```bash
   mvn clean install -U
   ```
4. Verify environment variables are set:
   ```bash
   echo $GITHUB_USERNAME
   echo $GITHUB_TOKEN
   ```

#### Authentication Errors

1. Check token expiration
2. Verify token scopes include `read:packages` and `write:packages`
3. Regenerate the token if needed
4. Check GitHub status: https://www.githubstatus.com/

#### Permission Errors

1. Ensure your GitHub account has access to the repository
2. Check repository settings in GitHub
3. Verify package visibility (public vs private)

### Alternative Authentication Methods

#### Using .netrc File

Create `~/.netrc`:
```
machine maven.pkg.github.com
login YOUR_GITHUB_USERNAME
password YOUR_GITHUB_TOKEN
```

#### Using GitHub CLI
```bash
gh auth login
# Then use Maven normally
mvn deploy
```

#### Using CI/CD (GitHub Actions)
```yaml
- name: Deploy to GitHub Packages
  run: mvn deploy
  env:
    GITHUB_USERNAME: ${{ github.actor }}
    GITHUB_TOKEN: ${{ secrets.GITHUB_TOKEN }}
```

### Support

For issues with GitHub Packages:
- GitHub Packages documentation: https://docs.github.com/en/packages
- Maven documentation: https://maven.apache.org/
- GitHub support: https://support.github.com/


## Eclipse Launch Configuration Instructions

This project is configured as an Eclipse plug-in with Maven support. Here's how to launch and debug it:

### Prerequisites
- Eclipse IDE for Java Developers or Eclipse IDE for Eclipse Committers
- Maven integration (m2eclipse)
- Plug-in Development Environment (PDE)

### Launch Configurations

Two launch configurations have been created in the `.launch` directory:

#### 1. Refactoring Advice Eclipse Application
- **Type**: Eclipse Application
- **Purpose**: Launches a new Eclipse instance with your plug-in installed
- **Usage**: Right-click → Run As → Refactoring Advice Eclipse Application
- **Debug**: Right-click → Debug As → Refactoring Advice Eclipse Application

#### 2. Refactoring Advice OSGi Framework
- **Type**: OSGi Framework
- **Purpose**: Launches an OSGi framework with your plug-in for testing
- **Usage**: Right-click → Run As → Refactoring Advice OSGi Framework
- **Debug**: Right-click → Debug As → Refactoring Advice OSGi Framework

### Importing the Project

1. In Eclipse: File → Import → Existing Maven Projects
2. Browse to this project directory
3. Select the project and import
4. Eclipse will automatically recognize it as a plug-in project

### Building

The project uses Maven for building:
- `mvn clean package` - builds the plug-in JAR
- `mvn install` - installs to local Maven repository
- `mvn deploy` - deploys to GitHub Packages (configured in pom.xml)

### Project Structure

The project follows the standard Maven directory layout:

```
src/
├── main/
│   ├── java/      - Main Java source code (compiled to target/classes)
│   └── resources/ - Main resources (copied to target/classes by Maven)
└── test/
    ├── java/      - Test Java source code (compiled to target/test-classes)
    └── resources/ - Test resources (copied to target/test-classes by Maven)
```

**In Eclipse:**
- `src/main/java` - Main source folder (root level)
- `src/main/resources` - Main resources folder (root level)
- `src/test/java` - Test source folder (root level)
- `src/test/resources` - Test resources folder (root level)
- All folders persist through Maven updates
- Test folders are marked with JUnit test icons
- No compiler errors - proper Maven source configuration

**Key Configuration:**
- `pom.xml` contains proper Maven source/resource configuration
- `build-helper-maven-plugin` ensures source folders persist
- Maven lifecycle mapping prevents overwrites
- Standard Maven directory layout with explicit configuration
- Configuration survives `mvn eclipse:eclipse` and project updates

### Key Configuration

- `.classpath` - Explicitly defines separate source folders for main/test
- `.project` - Contains both Maven and PDE natures
- `build.properties` - Configures plug-in build output

### Dependencies

The plug-in requires:
- `org.eclipse.swt`
- `org.eclipse.jface`
- `nl.ou.refactoring.advice` (from GitHub Packages)

**Maven Repository Configuration:**

To ensure Maven looks in the GitHub repository for dependencies:

1. **Add GitHub repository to your `settings.xml`:**
```xml
<settings>
  <profiles>
    <profile>
      <id>github</id>
      <repositories>
        <repository>
          <id>github</id>
          <name>GitHub Packages</name>
          <url>https://maven.pkg.github.com/Open-Universiteit-Refactoring-Diagnosis/GraphAdvice</url>
        </repository>
      </repositories>
    </profile>
  </profiles>
  <activeProfiles>
    <activeProfile>github</activeProfile>
  </activeProfiles>
</settings>
```

2. **Or activate the GitHub profile when building:**
```bash
mvn clean install -Pgithub
```

3. **Ensure authentication is configured** (see authentication section above)

**Troubleshooting:**
- If Maven still can't find the dependency, try:
  ```bash
  mvn clean install -U -Pgithub
  ```
- Check that the artifact exists in GitHub Packages
- Verify your authentication credentials

### Troubleshooting

If launch configurations don't appear:
1. Refresh the project (F5)
2. Check that the project has PDE nature (right-click → Configure → Convert to Plug-in Projects)
3. Ensure Maven dependencies are resolved (right-click → Maven → Update Project)