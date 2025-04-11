package pl.coderslab.cryptomanagement.generic;

import lombok.AllArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import jakarta.validation.Validator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
public abstract class GenericService<T> {
    protected final JpaRepository<T, Long> repository;
    protected final Validator validator;

    public ResponseEntity<List<T>> getAll() {
        return ResponseEntity.ok(repository.findAll());
    }

    public ResponseEntity<T> getById(Long id) {
        Optional<T> result = repository.findById(id);
        if (result.isPresent()) {
            return ResponseEntity.ok(result.get());
        } else {
            throw new RuntimeException();
        }
    }

    public ResponseEntity<T> add(T entity) {
        var violations = validator.validate(entity);
        if (violations.isEmpty()) {
            repository.save(entity);
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<T> delete(Long id) {
        Optional<T> result = repository.findById(id);
        if (result.isPresent()) {
            repository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            throw new RuntimeException();
        }
    }
}
