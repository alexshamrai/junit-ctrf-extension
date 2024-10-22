package io.github.alexshamrai.integration;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.extension.ExtendWith;
import io.github.alexshamrai.jupiter.CtrfExtension;

@ExtendWith(CtrfExtension.class)
@Tag("integration")
public abstract class BaseIntegrationTest {
}
