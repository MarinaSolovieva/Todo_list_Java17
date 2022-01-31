package com.example.todo.model.redis;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRedis implements Serializable {

    @Serial
    private static final long serialVersionUID = -1708052056529138029L;

    private Long id;
    private String name;
    private String surname;
}
