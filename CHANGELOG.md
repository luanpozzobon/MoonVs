<h1 align="center">Changelog - v0.2.0-beta.2024-02</h1>

## Added

- Rating of contents.
- View of user's rating and average rating for each content.
- Backend method 'addOrEditRating()' on RatingController and RatingService.
- Backend DTO 'RateRequest'.
- Backend DTO 'UserAccountRequest'.
- Backend exception 'IllegalIdException'.
- Backend Account methods 'getAccount' and 'updateUsernameAndOrEmail'.
- 'create()' methods on UserBuilder.
- Copy Constructor on User entity.
- 'create()' methods on ContentBuilder.
- Backend Content methods 'search', 'databaseSearch', 'tmdbSearch', 'viewContent', 'viewDatabaseContent' and 'viewTmdbContent'.
- SearchType enum.
- Response DTO.
- Backend Profile method 'getProfile'.
- Backend Rating method 'getUserRatingList'.
- Copy Constructor on Profile entity.
- 'create()' methods on Profilebuilder.
- RequestEntity.

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
- Backend Profile methods 'createProfile' and 'editProfile'.
- Backend Rating methods 'addOrEditRating', 'getUserRating' and 'getAvgRating'.
- Included 'page' and 'totalPages' field in TmdbResults.
- The Tmdb Search now uses the user age, and includes results of more than one page.
- Added method 'isOfLegalAge' in User entity.

## Deprecated

- Backend method 'newRating()' from RatingController and RatingService.
- Backend DTO 'RatingRequest'.
- Backend DTO 'AccountRequest'.
- Backend Account methods 'account', 'updateAccount', 'updatePassword', and 'deleteAccount'.
- Backend DTO 'RegisterRequest' and 'RegisterResponse'.
- UserBuilder method 'withRegisterDto'.
- Backend Auth methods: 'login' and 'register'.
- Backend Content methods 'internalSearch', 'externalSearch', 'internalContent' and 'externalContent'
- Backend Profile methods 'createProfile', 'seeProfile' and 'editProfile'.
- Backend Rating method 'getAllUserRatings'.
- ProfileBuilder constructors and methods 'createProfile' and 'editProfile'.
- 'search' method of TmdbService.
- HttpRequestEntity.

## Fixed

- Fixed the URL to 'About MoonVs'.
- Fixed replacement of blank spaces on the search methods!
- External links now open on a blank tab.
- Fixed bug of titles in list of watch providers.
- Fixed NullPointerException on ProfileBuilder method 'withBio'.
- Fixed bug that didn't allow to delete account with existing ratings.
- Fixed bug that wouldn't allow to create new ratings.

[You can also access the full changelog](https://luanpozzobon.github.io/luanpozzobon_site/pages/projects/moonvs/moonvs.html)