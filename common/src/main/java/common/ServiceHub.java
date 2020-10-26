package common;

import lombok.RequiredArgsConstructor;

import java.util.ServiceConfigurationError;
import java.util.ServiceLoader;

@RequiredArgsConstructor
public class ServiceHub {
    private final ModuleLayer layer;

    public <T> ServiceLoader<T> load(Class<T> cls) {
        return ServiceLoader.load(layer, cls);
    }

    public <T> T loadFirst(Class<T> interfaceClass) {
        return ServiceLoader.load(layer, interfaceClass).findFirst()
                .orElseThrow(() -> {
                    String msg = String.format("No implementation of service interface class '%s' found",
                            interfaceClass.getName());
                    return new ServiceConfigurationError(msg);
                });
    }
}
