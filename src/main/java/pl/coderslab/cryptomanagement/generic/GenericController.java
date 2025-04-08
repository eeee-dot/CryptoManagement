package pl.coderslab.cryptomanagement.generic;

import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
public class GenericController<T>{
    protected final GenericService<T> service;
    protected final Class<T> type;

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
    @PostMapping("/generic/add")
    public ResponseEntity<T> add(@RequestBody T object) {
        return service.add(object);
    }

    @Operation(
            summary = "Delete instance by ID",
            description = "Deletes instance with given ID"
    )
    @DeleteMapping("/generic/delete/{id}")
    public ResponseEntity<T> delete(@PathVariable Long id) {
        return service.delete(id);
    }

}
