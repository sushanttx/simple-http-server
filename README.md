# Simple HTTP Server

A lightweight, customizable HTTP server implementation in Java that follows HTTP/1.1 specifications. This project demonstrates core HTTP protocol concepts and provides a foundation for building web applications.

## Features

- HTTP/1.1 compliant request parsing
- Support for GET and HEAD methods
- Header parsing with RFC-compliant validation
- HTTP version compatibility handling
- Thread-safe request processing
- Detailed error handling with appropriate HTTP status codes

## Architecture

### Core Components

1. **HttpParser**
   - Parses incoming HTTP requests
   - Validates request line, headers, and body
   - Implements RFC-compliant parsing rules
   - Handles HTTP version compatibility

2. **HttpRequest**
   - Represents parsed HTTP requests
   - Stores method, target, version, and headers
   - Provides header access methods
   - Handles HTTP version compatibility

3. **HttpMethod**
   - Enum representing supported HTTP methods
   - Currently supports GET and HEAD
   - Enforces method length limits

4. **HttpVersion**
   - Manages HTTP version compatibility
   - Supports HTTP/1.1
   - Handles version negotiation

### Request Processing Flow

1. Raw request received as InputStream
2. Request line parsed (method, target, version)
3. Headers parsed and validated
4. Request object created and populated
5. Response generated based on request

## Implementation Details

### Header Parsing
- Uses RFC-compliant regex patterns
- Validates header field names and values
- Case-insensitive header names
- Supports standard HTTP header formats

### Error Handling
- HTTP 400 Bad Request for malformed requests
- HTTP 501 Not Implemented for unsupported methods
- HTTP 505 Version Not Supported for incompatible versions
- Detailed error messages and logging

## Testing

The project includes comprehensive test coverage:
- Request line parsing tests
- Header parsing tests
- HTTP version compatibility tests
- Error handling tests
- Edge case validation

## Usage

```java
// Create and configure the server
HttpServer server = new HttpServer();
server.start();

// Handle incoming requests
HttpRequest request = httpParser.parseHttpRequest(inputStream);
// Process request and generate response
```

## Requirements

- Java 8 or higher
- JUnit 5 for testing
- SLF4J for logging

## Building

```bash
# Compile the project
javac -d target/classes src/main/java/com/scratch/http/*.java

# Run tests
java -cp target/classes:target/test-classes org.junit.platform.console.ConsoleLauncher
```

## Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request
