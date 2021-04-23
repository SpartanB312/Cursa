package club.deneb.client.utils.clazz;

import club.deneb.client.features.Module;

public class ActiveModule {
    public Module mod;
    public String string;

    public ActiveModule(Module module, String string) {
        this.mod = module;
        this.string = string;
    }

    public String string() {
        return string;
    }
}