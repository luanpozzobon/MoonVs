<h1 align="center">Changelog - v0.2.0-beta.2024-02</h1>

## Added

- Rating of contents.
- View of user's rating and average rating for each content.
- Backend method 'addOrEditRating()' on RatingController and RatingService.
- Backend DTO 'RateRequest'.
- Backend DTOs 'UserAccountRequest' and 'UserAccountResponse'.
- Backend exception 'IllegalIdException'.
- Backend Account methods 'getAccount' and 'updateUsernameAndOrEmail'.
- 'create()' methods on UserBuilder.
- 'create()' methods on ContentBuilder.
- Backend Content methods 'search', 'databaseSearch', 'tmdbSearch', 'viewContent', 'viewDatabaseContent' and 'viewTmdbContent'.
- SearchType enum.
- Response DTO.

## Changed

- Logo moved to the form area on the Auth screen.
- Changed forms background color on Auth screen.
- Changed input fields color on Auth screen.
- Changed effect on selected tab at Auth screen.
- Changed input fields color on Account screen.
- Changed input fields border.
- Moved 'JustWatch' attribution only to 'Where to Watch?' section.
- AccountController now performs basic validations.
- Backend Account methods 'updatePassword' and 'deleteAccount'.
- Backend Auth methods: 'login' and 'register'.

## Deprecated

- Backend method 'newRating()' from RatingController and RatingService.
- Backend DTO 'RatingRequest'.
- Backend DTO 'AccountRequest'.
- Backend Account methods 'account', 'updateAccount', 'updatePassword', and 'deleteAccount'.
- Backend DTO 'RegisterRequest' and 'RegisterResponse'.
- UserBuilder method 'withRegisterDto'.
- Backend Auth methods: 'login' and 'register'.
- Backend Content methods 'internalSearch', 'externalSearch', 'internalContent' and 'externalContent'

## Fixed

- Fixed the URL to 'About MoonVs'.
- Fixed replacement of blank spaces on the search methods!
- External links now open on a blank tab.
- Fixed bug of titles in list of watch providers.

[You can also access the full changelog](https://luanpozzobon.github.io/luanpozzobon_site/pages/projects/moonvs/moonvs.html)