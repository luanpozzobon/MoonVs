![MoonVs Logo](/assets/Logo-Black.png)

A modern system to manage the titles you want to watch, or those you have already watched!

![Static Badge](https://img.shields.io/badge/version-1.0.0-white?style=for-the-badge&labelColor=black)
[![Static Badge](https://img.shields.io/badge/license-MIT-white?style=for-the-badge&labelColor=black)](./LICENSE)

---

This is a modern and robust system to manage movies and tv shows, it will count with a RESTful API, a WEB page and a mobile Android App.

Currently, the project includes only an initial version of the REST API, with all its documentation, allowing early users to experiment the project even though it's still not released.

---

## Project History

This is an evolution of a previous beta version. The project was completely re-built from scratch strategically, looking for an architecture to ease the scalability and mainteinance. 
This, and other technical decisions are explained in the [Thecnical Decisions Document](docs/TECH_DECISIONS.md).

**Beta Version**: [MoonVs 0.3.1-beta.2024-04](https://github.com/luanpozzobon/MoonVs)

---

## Features

- [x] Creation and management of playlists (API)
- [x] Addition and remotion of titles into these playlists (API)
- [x] Visualization of these titles (API)
- [ ] Search for movies/TV shows via TMDb API integration.
- [ ] Visualization of informations about movies and tv shows
- [ ] Automatic watchlists
- [ ] Rating of watched movies and tv shows
- [ ] Detailed reports and dashboards of the user's watched titles
- [ ] Interface WEB
- [ ] Android App

---

## Technologies
**Back-End**:
- [Java 21](https://www.java.com/en/)
- [Spring Boot 3.x](https://spring.io/)
- [SpringDoc OpenAPI 2.8.9](https://springdoc.org) (API Documentation)
- [Maven](https://maven.apache.org) (Build and Dependency Management)

**Front-End** (Future):
- [Angular](https://angular.dev)

**Databases**:
- [PostgreSQL](https://www.postgresql.org)

**Other**:
- [Git](https://git-scm.com)
- [Docker](https://www.docker.com)
- [Google Cloud Platform](https://cloud.google.com) (API Deployment)
- [Aiven](https://aiven.io) (Database Hosting)
- [SemVer](https://semver.org/) (Semantic Versioning)

---

## How to Access

You can access and test the project via its [Swagger UI](https://moonvs-239895162731.us-central1.run.app/swagger-ui.html).

Since it's hosted in the free tier of GCP, it may take a while to load the first time (up to 10 seconds).

## Roadmap

This project is under continuous development, as so here is a planning of the next steps:

### Phase 1 (Current)

- [x] Basic API development
- [x] Documentation
- [ ] Tests

### Phase 2

- [ ] Integration with TMDb API
- [ ] Searching and visualization of useful data about titles

### Phase 3

- [ ] Addition of a Web Interface
- [ ] Public user profile
- [ ] Rating system


### Long Term

- Personalized recommendations
- Dashboards and data reports

---

## Technical Decisions

For a detailed explanation of the architectural and technical decisions behind the project, please refer to the dedicated [Technical Decisions Document](docs/TECH_DECISIONS.md). This document explains the reasons behind every choice and key implementation.


## License

This project is licensed under the [MIT License](LICENSE)

## Author

<table>
    <tr>
        <td align="center">
            <a href="http://github.com/luanpozzobon">
            <img src="https://avatars.githubusercontent.com/u/108753073?v=4" width="100px;" alt="GitHub photo of Luan Pozzobon"/><br>
            <sub>
                <b>luanpozzobon</b>
            </sub>
            </a>
        </td>
        <td align="center">
            <a href="https://www.linkedin.com/in/luanpozzobon/">
            <img src="https://media.licdn.com/dms/image/v2/D4D03AQFW0wMXnNIOZw/profile-displayphoto-shrink_800_800/profile-displayphoto-shrink_800_800/0/1711368855052?e=1756339200&v=beta&t=ECNa-G2AvvuhpHO1o4CVmZXcS7oykelAzm0lGHexS1g" width="100px;" alt="LinkedIn photo of Luan Pozzobon"/><br>
            <sub>
                <b>LinkedIn: Luan Pozzobon</b>
            </sub>
            </a>
        </td>
    </tr>
</table>