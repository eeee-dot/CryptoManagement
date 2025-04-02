package pl.coderslab.cryptomanagement.generic;

import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@AllArgsConstructor
public class GenericController<T>{
    protected final GenericService<T> service;
    protected final Class<T> type;
    private final String name = type.getSimpleName();

    @Operation(
            summary = "Get all instances",
            description = "Returns list of all instances")
    @GetMapping("/get")
    public ResponseEntity<List<T>> findAll() {
        return service.getAll();
    }

    @Operation(
            summary = "Get instance by ID",
            description = "Returns instance by ID"
    )
    @GetMapping("/get/{id}")
    public ResponseEntity<T> findById(@PathVariable Long id) {
        return service.getById(id);
    }

    @Operation(
            summary = "Add new instance",
            description = "Adding new instance to entity"
    )
    @PostMapping("/add")
    public ResponseEntity<T> add(@RequestBody T object) {
        return service.add(object);
    }


}
