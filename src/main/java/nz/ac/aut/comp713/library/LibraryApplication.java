package nz.ac.aut.comp713.library;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.info.Info;

@ApplicationPath("/api/v1")
@OpenAPIDefinition(
    info = @Info(
        title = "Library Management API",
        version = "1.0",
        description = "REST API for the Library Management System"
    )
)
public class LibraryApplication extends Application {
}