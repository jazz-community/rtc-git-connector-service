# RTC Git Connector Service
Service providing server side functionality for the [RTC Git Connector](https://github.com/jazz-community/rtc-git-connector). Currently, this involves providing custom link styles and rich hover functionality for links to Gitlab artifacts.

-   [Setup](#setup)
    -   [Prerequisites](#prerequisites)
    -   [Installation](#installation)
    -   [Contributing](#contributing)
    -   [Licensing](#licensing)

# Setup
Deploying this service only makes sense for supporting the UI functionality in the [RTC Git Connector](https://github.com/jazz-community/rtc-git-connector). Detailed instructions for installing RTC Git Connector can be taken from it's [readme](https://github.com/jazz-community/rtc-git-connector/blob/master/README.md).

## Prerequisites
RTC Git Connector Service requires that [Secure User Property Store for RTC](https://github.com/jazz-community/rtc-secure-user-property-store) has been **installed** and **properly configured**. This is a compulsory dependency and RTC Git Connector Service will not work at all without the Secure User Property Store being available.

RTC Git Connector Service has been developed, tested and deployed on RTC versions above 6.0.3.

## Installation
Please refer to the [Releases page](https://github.com/jazz-community/rtc-git-connector-service/releases) for current stable releases. The plugin can be installed like any other rtc update-site, for detailed instructions, you can follow [these steps](https://github.com/jazz-community/rtc-create-child-item-plugin#installation).

## Contributing
Please use the [Issue Tracker](https://github.com/jazz-community/rtc-git-connector-service/issues) of this repository to report issues or suggest enhancements.

For general contribution guidelines, please refer to [CONTRIBUTING.md](https://github.com/jazz-community/rtc-git-connector-service/blob/master/CONTRIBUTING.md)

## Licensing
Copyright (c) Siemens AG. All rights reserved.<br>
Licensed under the [MIT](LICENSE) License.
