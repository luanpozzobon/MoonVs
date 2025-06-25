# Architectural and Technical Decisions

This document outlines the key architectural and technical decisions made during the development of the project, explaining the rationale, alternatives considered, and any significant implications or trade-offs.

---

## Tech Stack (API)
**Context**: The project needs well-stablished technologies, allowing to scale the application, whilst maintaining performance and security. Also, a strong community support is interesting.

**Decision**: The API was developed using **Java with Spring Boot**.

**Rationale**
- **Maturity**: Java is a mature language with a strong ecossystem, allowing to make almost everything imaginable.
- **Community**: Java and Spring have both a grat community, allowing for easy problem solving.

**Trade-Offs**
- **Memory Overhead**: When compared to some other languages, Java may have a higher memory consumption, mainly because of the JVM.

---

## DDD and Clean Architecture (API)
**Context**: The project requires an architecture that facilitate its maintenance and scalability.

**Decision**: The API was developed following the **DDD and Clean Architecture** principles.

**Rationale**
- **Maintenance & Scalability**: An API developed following these principles, offers a good separation of concerns, allowing for an easier maintainance and scalability.
- **Dependency Inversion**: One of the principles, the DI, also facilitate in the scalability, since the business don't depend on any external sources, it's easier to change any infrastructural thecnology.

**Trade-Offs**
- **Overhead in small applications**: At the beginning of a project, there are lots of files and layers, which seems a bit too much, but when the API starts to scale, it is possible to see the ease to maintain.

**Alternatives**
- **MVC**: Easier to implement and without the initial overhead, but when the application starts to grow, can become messy and dirty quickly, due to a coupling between controllers and business rules.

