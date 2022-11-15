# familie-baks-infotrygd-feed
Applikasjon for å publisere barnetrygd og kontantstøtte hendelser til infotrygd

# Kjøring lokalt
`DevLauncher` kjører appen lokalt med Spring-profilen `dev` satt. Appen tilgjengeliggjøres da på `localhost:8094`.

### Database
Dersom man vil kjøre appen med postgres, kan man kjøre `DevLauncherPostgres` med Spring-profilen `postgres` satt. 

Da må man sette opp postgres-databasen, dette gjøres slik:
```
docker run --name familie-ba-infotrygd-feed -e POSTGRES_PASSWORD=test -d -p 5432:5432 postgres
docker ps (finn container id)
docker exec -it <container_id> bash
psql -U postgres
CREATE DATABASE "familie-baks-infotrygd-feed";
```

## Kontaktinformasjon
For NAV-interne kan henvendelser om applikasjonen rettes til #team-familie på slack. Ellers kan man opprette et issue her på github.
