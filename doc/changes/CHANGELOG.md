# Changelog
This changelog is primarily to allow F-Droid to document changes with each new version.

See [keepachangelog.com](https://keepachangelog.com/en/1.0.0/) for information of the format.

This project roughly utilizes [Semantic Versioning](https://semver.org/spec/v2.0.0.html).  In some versions the PATCH of MAJOR.MINOR.PATCH is omitted.  Note also that unreleased versions are omitted.

## [2.35.1] - 2025-05-07
### Fixed
- Changed remaining AlertDialogs to material design
- Improve visibility of cycle length result in dark mode
* Remove some redundant references in dialog boxes

## [2.35.0] - 2025-04-28
### Added
- BMI calculator
### Changes
- Target SDK Android 35
- Edge to edge UI
- Material design dialog boxes

## [2.34.2] - 2025-03-01
### Fixed
- HTML pages not appearing correctly in dark mode

## [2.34.1] - 2024-12-10
### Fixed
- Off center main screen when using large display size [issue #42]

## [2.34.0] - 2024-11-29
### Added
- HCM Risk Scores group to Risk Scores
- HCM SCD 2022 risk score
- HCM SCD 2024 risk score
### Changes
- Rename HCM SCD 2014 to HCM SCD Risk 2014
- Update instructions for HCM SCD Risk 2014 risk score

## [2.33.0] - 2024-07-14
### Changes
- Target SDK version changed to Android 34, as required by Google

## [2.32.1] - 2024-05-25
### Fixed
- Divide by zero error in V2 Transtion Calculator

## [2.32.0] - 2024-05-25
### Added
- V2 Transition Ratio

## [2.31.0] - 2023-10-25
### Added
- Groningen Frailty Indicator

## [2.30.3] - 2023-02-12
### Changes
- Updated privacy policy and added link to About dialog

## [2.30.2] - 2022-11-07
### Fixed
- Erroneous calculation in APPLE score

## [2.30.1] - 2022-10-13
### Fixed
- Removed no longer needed Instruction button in Atrial Tach Locaiton module

## [2.30.0] - 2022-08-18
### Fixed
- All references reviewed, updated, and converted to DOI links when available
- Further fixes for dark mode compatibility
- Various bug fixes
### Added
- GFR calculator, using CKD-EPI formula
- APPLE score, to estimate risk of recurrent AF post-ablation
- All risk score results can now be copied to clipboard
### Changes
- New home screen
- Instructions and references are now menu items

## [2.29.1] - 2022-08-30
### Fixed
- Fix bottom system toolbar not being dark in dark mode on Android 10
- Fix bug in Entrainment calculator

## [2.29.0] - 2022-08-20
### Changes
- Update QTm and QTmc to 2020 update of Bogossian formula
- QTc formula can now be selected in QTc with IVCD module
### Fixed
- Minor formatting changes in QTc calculators
- Text in QTc IVCD results reflects recommended QTc formulas to be
  used with each IVCD correction formula

## [2.28.0] - 2022-07-24
### Fixed
- Fixed gap in Warfarin calculator dose ranges

## [2.27.0] - 2022-05-27
### Changes
- Update ARVC calculator to reflect new parameters based on updated reference
- Update Entrainment map to show when site is likely to be successful

## [2.26.1] - 2022-03-07
### Fixed
- Broken link to reference for Warfarin Clinic calculator

## [2.26.0] - 2021-06-19
### Fixed
- Enlarged LQT ECG
- Fixed problem with dark mode
### Added
- Android 11 support

## [2.25.4] - 2021-04-14
### Fixed
- Fixed regression that disabled maintenance of screen position of
  html-based view on rotation.
- Fixed problems with dark mode of html views.
- Fixed view too wide with rotation of html views.

## [2.25.3] - 2020-09-18
### Changes
- Provide night mode images

## [2.25.2] - 2020-09-07
### Fixed
- Fixed web views being stuck in dark mode

## [2.25.1] - 2020-09-05
### Fixed
- Fixed crash on pre-KitKat devices

## [2.25] - 2020-09-03
### Fixed
- Fixed some typos.
### Added
- Support dark mode.
### Changes
- New launcher icons (white on black).
- Center certain views on screen.
- Update Chads, ChadsVasc risks.
- Update Brugada drugs url.
- Buttons are now borderless and at bottom of screen.
- Pass lint tests.

## [2.24] - 2020-08-07
### Fixed
- Fixed web view going back to top of page when rotating device.

## [2.23] - 2020-04-06
### Added
- QT prolongation risk score.

## [2.22] - 2019-08-17
### Added
- Long QT Syndromes (All).

### Changes
- Long QT subtypes renamed Long QT Syndromes (Common)

## [2.21] - 2019-04-01
### Added
- ARVC calculator.

## [2.20] - 2018-10-02
### Added
- PreLBBB QTc formula.

## [2.19] - 2018-08-31
### Added
- Cardiac tamponade module.

## [2.18] - 2018-07-08
### Added
- LBBB criteria.

### Changes
- Minimum SDK changed to 14.  Target SDK 26.

## [2.17.1] - 2018-02-26
### Fixed
- Back arrow appearing on main menu screen.

## [2.17] - 2018-02-21
### Added
- 2018 CMS module.

## [2.16] - 2017-09-30
### Added
- ERS score.
- Brugada Shanghai score.
- Brugada diagnosis.

### Fixed
- Toolbar colors.
- Other bug fixes.

## [2.15] - 2016-09-19
### Added
- ICD mortality risk score.
### Changes
- Cycle length calculator layout.

## [2.13] - 2015-12-04
### Added
- ATRIA bleeding score.
- ATRIA stroke score.
- SAMe-TT2R2 score.

## [2.12] - 2015-07-13
### Added
- Added message deprecating use of CHADS2 score.

### Changes
- Updated CHA2DS2-VASc messages to reflect latest guidelines.
- Edoxaban added to list of recommended anticoagulants.

## [2.11.1] - 2015-06-04
### Fixed
- Fixed misspellings.

Changes since v2.10
- Added HCM-SCD risk calculator (ESC 2014 guidelines).
- Modified icons.
- Minor menu restructuring and code refactoring.

## [2.11] - 2015-05-29
### Added
- Added HCM-SCD risk calculator (ESC 2014 guidelines).

### Changes
- Modified icons.
- Minor menu restructuring and code refactoring.

## [2.10] - 2015-03-14
### Added
- Added standalone creatinine clearance calculator.
- Added right ventricular hypertrophy criteria.
- Added more information to results of drug dosing calculators.
- Added drug reference pages with detailed use of EP related drugs.

### Changes
- Updated apixaban dosing calculator dosing for end-stage renal disease.
- Creatinine clearance toolbar in drug reference pages allows easy access to creatinine clearance data while reviewing dosing information.

## [2.9] - 2015-02-08
### Added
- D'Avila WPW algorithm.

### Changes
- Clarified text of Arruda algorithms.

## [2.8] - 2015-01-16
### Added
- Edoxaban drug dose calculator.

## [2.7.1] - 2014-12-14
### Fixed
- Fixed Android bug causing crashes in Samsung devices running Android 4.2.

## [2.7] - 2014-11-19
### Changes
- Material Design for all versions!

## [2.6] - 2014-10-21
### Added
- Para-Hisian pacing and RV apex vs base pacing to References and Tools.

## [2.5.1] - 2014-10-18
### Added
- Android 5.0 Lollipop Material Design theme.

## [2.5] - 2014-08-02
### Changes
- Changed to holo-light theme for Android v 4.0 and higher.
- Other UI tweaks.

## [2.4] - 2014-06-13
### Changes
- Copy risk score results to the clipboard with the "Copy Result".
  button for pasting into reports.

## [2.3.3] - 2014-04-12
### Fixed
- Bug fixes

## [2.3.2] - 2014-04-11
### Fixed
- Bug fixes

## [2.3.1] - 2014-03-25
### Added
- Vereckei wide complex tachycardia algorithm.
- ICD in-hospital complication risk score (Dodson JA et al, JACC
  2014 63:788).

## [2.2.2] - 2014-01-14
### Fixed
- Fixed broken Long QT drugs link

## [2.2.1] - 2013-07-18
### Fixed
- Fixed wrong reference in EGSYS Score

## [2.2] - 2013-07-17
### Fixed
- Added Syncope Risk Scores: San Francisco Rule, Martin Algorithm, EGSYS Score and OESIL Score to risk stratify patients with syncope.

## [2.1] - 2013-06-12
### Changed
- Weight Calculator now suggests which weight to use. 

## [2.0.1] - 2013-04-04
### Changes
- Fixed spelling errors

## [2.0] - 2013-03-17
### Added
- Added Atrial Tachycardia Location module
- Added Date Calculator
- Added Entrainment Mapping module
- Added external links for Brugada and Long QT drugs

### Changed
- Updated Apixaban calculator to match final package insert
- Reference section is now Reference & Tools
- Improved formatting of calculator results
- Bumped version up to 2.0 because of major updates and to synchronize versions across platforms

## [1.5.1] - 2013-02-09
### Removed
- Removed Entrainment Mapping from Reference section.  It appeared prematurely.  (Entrainment Mapping module will appear in next update).

## [1.5] - 2013-02-07
### Changes
- Can now change QTc formula within QTc calculator
- Long QT Diagnosis now uses updated 2011 Schwartz score

## [1.4] - 2013-01-01
### Added
- Apixaban drug dose calculator.

### Changed
- Drug dose calculators give warning for pediatric patients.

## [1.3.2] - 2012-12-23
### Fixed
- Fixed Warfarin dosing table crash.
- Fixed crash in Honeycomb due to null ActionBar.

## [1.3.1] - 2012-11-05
### Added
- Added Up Button behavior for Honeycomb and beyond.

### Changed
- New layout and name for Interval/Rate Conversion.
- Dosing in Warfarin dose table now distributed better.

### Fixed
- Fixed some spelling errors discovered while developing iOS version.

## [1.3] - 2012-09-02
### Added
- Instructions added to Warfarin Clinic calculator.

### Changed
- Warfarin dose tables generated for wider range of doses.
- Calculated doses are given in Warfarin Clinic calculator.
- Micromol/L units for creatinine available in drug dose calculators.
- Cycle Length Calculator now rounds instead of truncates result.

### Fixed
- Fixed endless loop crash in Warfarin dose table.
- Other minor fixes.

## [1.2] - 2012-04-14
### Added
- Sotalol Dose Calculator.

### Fixed
- Fixed typo in Long QT Subtypes module.

## [1.1] - 2012-03-27
### Added
- Added Epicardial vs Endocardial VT module.
- Added Outflow Tract VT Location module.
- Added Mitral Annular VT Location module.

### Fixed
- Minor bug, spelling fixes.

## [1.0] - 2012-02-11
### Added
- Notes button to Brugada ECG patterns.
- Holo Theme style elements to various modules (for Android 3.0+).
- Error checking dialog to Estes LVH module.
- Source code link to About dialog.

### Changed
- Restructured menu lists.

### Fixed
- Fixed bug in ARVC/D 2010 criteria (echo minor fractional shortening criterion was incorrect).

### Removed
- Eliminated right facing arrows in lists, per new Android style guidelines.

## [0.13] - 2012-01-21
### Added
- Long QT Subtypes module.
- Long QT ECG Patterns module.
- Brugada Syndrome ECG Patterns module.

### Changed
- Long QT Diagnosis is now a list.
- Changed AV annulus figure, using public domain figure.
- Changed README.md (in source code at GitHub) to credit figure sources.

### Fixed
- Fixed errors in wide complex tachycardia figures.

## [0.12] - 2012-01-10
### Added
- LVH Criteria, including Romhilt-Estes point score.

## [0.11] - 2012-01-07
### Added
- Added 1994 ARVC/D diagnostic criteria (simpler than 2010 criteria).

### Fixed
- Fixed wrong title in Long QT Syndrome result dialog box.
- Minor refactoring and formatting fixes.

## [0.10] - 2012-01-01
### Added
- Long QT Diagnosis module.

### Fixed
- Corrected variations in text sizes in similar modules.

## [0.9] - 2011-12-19
### Added
- Added HEMORR2HAGES bleeding risk score.
- Added WPW Pathway Location group.
- Added Arruda algorithm.
- Added Modified Arruda algorithm.
- Added Milstein algorithm.
- Added Anatomy of AV Annulus map.
### Changed
- Updated ChadsVasc risk percentages and reference.

## [0.8] - 2011-12-08
- Lowered minimum Android version to 2.1.
### Added
- Added CMS ICD Criteria module.
- Added HCM Sudden Death Risk Score module.

### Changed
- Program runs on up to Android version 4.0 Ice Cream Sandwich.
- Risk Scores now grouped.
- Updated About dialog.
- Various cosmetic tweaks.

## [0.7.1] - 2011-12-05
### Fixed
- Fixed dofetilide dosing units from mg to mcg.  This was a regression with version 0.7 and was correct in earlier versions.

## [0.7] - 2011-11-24
### Added
- Added Rivaroxaban Calculator. 
- Added Short QT Syndrome Diagnosis  

###Changed
- Minor formatting changes and bug fixes.

### Fixed
- Fixed alignment in WCT morphology figures.
- Grouped drug dose calculators together.

## [0.6] - 2011-11-12
### Added
- Incorporated new (November 2011) US dabigatran guidelines into
  Dabigatran Calculator.

### Changed
- Drug calculator UI reformatted
- Drug calculator creatinine clearance and warnings colorized
- Weight units (kg or lb) selectable in drug calculators

## [0.5] - 2011-10-29
### Added
- EP Normal Values.
- ARVC/D Diagnosis based on 2010 Task Force Revised Criteria.

### Changed
- Compressed figures.

## [0.4] - 2011-10-06
### Added
- Wide Complex Tachycardia Algorithms (Brugada, RWPT, Morphology).
- Images to illustrate ECG morphologies.

### Changed
- QTc Calculator now uses Heart Rate or RR Interval.
- Some general "prettifying" of layouts.

### Fixed
- Fixed wrong icon sizes.

## [0.3.1] - 2011-09-17
### Added
- Dabigatran Calculator.
- Warfarin Calculator.

### Changed
- QTc Calculator learned the Hodges QTc formula.
- Calculators now round to nearest integer for (very slightly!) more accurate
  result.

### Removed
- Removed "About" from module list.

## [0.2.1] - 2011-08-28
### Changed
- Fixed spelling errors.

## [0.2] - 2011-08-28
### Added
- Added CHADS, CHADS-VASc, HAS-BLED modules.
- Added maximum QTc to settings.

### Changed
- Changed user interface, to allow more modules.

## [0.1] - 2011-08-25
### Added
- First version!

