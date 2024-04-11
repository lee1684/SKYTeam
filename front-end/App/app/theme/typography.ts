// TODO: write documentation about fonts and typography along with guides on how to add custom fonts in own
// markdown file and add links from here

import { Platform } from "react-native"

const pretendardThin = require("../../assets/fonts/Pretendard-Thin.ttf")
const pretendardExtraLight = require("../../assets/fonts/Pretendard-ExtraLight.ttf")
const pretendardLight = require("../../assets/fonts/Pretendard-Light.ttf")
const pretendardRegular = require("../../assets/fonts/Pretendard-Regular.ttf")
const pretendardMedium = require("../../assets/fonts/Pretendard-Medium.ttf")
const pretendardSemiBold = require("../../assets/fonts/Pretendard-SemiBold.ttf")
const pretendardBold = require("../../assets/fonts/Pretendard-Bold.ttf")
const pretendardExtraBold = require("../../assets/fonts/Pretendard-ExtraBold.ttf")
const pretendardBlack = require("../../assets/fonts/Pretendard-Black.ttf")

export const customFontsToLoad = {
  pretendardThin,
  pretendardExtraLight,
  pretendardLight,
  pretendardRegular,
  pretendardMedium,
  pretendardSemiBold,
  pretendardBold,
  pretendardExtraBold,
  pretendardBlack,
}

const fonts = {
  pretendard: {
    thin: "pretendardThin",
    extraLight: "pretendardExtraLight",
    light: "pretendardLight",
    regular: "pretendardRegular",
    medium: "pretendardMedium",
    semiBold: "pretendardSemiBold",
    bold: "pretendardBold",
    extraBold: "pretendardExtraBold",
    black: "pretendardBlack",
  },
  helveticaNeue: {
    // iOS only font.
    thin: "HelveticaNeue-Thin",
    light: "HelveticaNeue-Light",
    normal: "Helvetica Neue",
    medium: "HelveticaNeue-Medium",
  },
  courier: {
    // iOS only font.
    normal: "Courier",
  },
  sansSerif: {
    // Android only font.
    thin: "sans-serif-thin",
    light: "sans-serif-light",
    normal: "sans-serif",
    medium: "sans-serif-medium",
  },
  monospace: {
    // Android only font.
    normal: "monospace",
  },
}

export const typography = {
  /**
   * The fonts are available to use, but prefer using the semantic name.
   */
  fonts,
  /**
   * The primary font. Used in most places.
   */
  primary: fonts.pretendard,
  /**
   * An alternate font used for perhaps titles and stuff.
   */
  secondary: Platform.select({ ios: fonts.helveticaNeue, android: fonts.sansSerif }),
  /**
   * Lets get fancy with a monospace font!
   */
  code: Platform.select({ ios: fonts.courier, android: fonts.monospace }),
}
