
plugins {
    `java-library`
}

repositories {
    mavenCentral()
}

dependencies {

    api("javax.jms:javax.jms-api:2.0.1")
    api("javax.servlet:servlet-api:2.4")

    api("com.caucho:hessian:4.0.66")
    api("de.ruedigermoeller:fst:2.56")
    api("com.esotericsoftware:kryo:5.3.0")

    val jboss_version = "2.0.12.Final"

    api("org.jboss.marshalling:jboss-marshalling:${jboss_version}")
    api("org.jboss.marshalling:jboss-marshalling-serial:${jboss_version}")
    api("org.jboss.marshalling:jboss-marshalling-river:${jboss_version}")

    api("aopalliance:aopalliance:1.0")
    api("com.google.dexmaker:dexmaker:1.2")
    api("net.bytebuddy:byte-buddy:1.12.10")
    api("cglib:cglib:3.3.0")

    val spring_version = "5.3.21"

    api("org.springframework:spring-core:${spring_version}")
    api("org.springframework:spring-beans:${spring_version}")
    api("org.springframework:spring-context:${spring_version}")
    api("org.springframework:spring-web:${spring_version}")

    implementation(project(":sugar-cubes-library"))
    implementation("org.objenesis:objenesis:3.2")

    testImplementation("org.apache.commons:commons-lang3:3.12.0")
    testImplementation("com.google.guava:guava:31.1-jre")
    testImplementation("org.springframework:spring-core:5.3.21")
    testImplementation("org.hamcrest:hamcrest:2.2")
    testImplementation("org.mockito:mockito-junit-jupiter:4.6.1")
    testImplementation("org.junit.jupiter:junit-jupiter:5.8.1")

}

tasks.named<Test>("test") {
    useJUnitPlatform()
}
