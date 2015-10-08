# RecyclerViewLoadMore

## Overview

Pull up to load more, show a message if no more, pull down to refresh

#### How it works
* Pull up to load 20 more data.
* Show a message if no more data. If random.nextInt(20) <= 5, there is no more data.
* Pull down to refresh. This will reset the load.
