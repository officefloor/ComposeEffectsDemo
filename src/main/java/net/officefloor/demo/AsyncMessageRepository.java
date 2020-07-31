package net.officefloor.demo;

import net.officefloor.plugin.clazz.Dependency;
import net.officefloor.r2dbc.R2dbcSource;
import reactor.core.publisher.Mono;
import scala.util.Either;
import scala.util.Left;
import scala.util.Right;

import java.util.function.Consumer;

public class AsyncMessageRepository {

    private @Dependency R2dbcSource source;

    public void getMessage(Integer id, Consumer<Either<Throwable, Message>> callback) {
        source.getConnection()
                .flatMap(connection -> Mono.from(connection.createStatement("SELECT ID, CONTENT FROM MESSAGE").execute()))
                .map(result -> result.map((row, meta) -> {
                    Message message = new Message();
                    message.setId(row.get("ID", Integer.class));
                    message.setContent(row.get("CONTENT", String.class));
                    return message;
                }))
                .flatMap(message -> Mono.from(message))
                .subscribe(message -> callback.accept(new Right<>(message)), error -> callback.accept(new Left<>(error)));
    }
}