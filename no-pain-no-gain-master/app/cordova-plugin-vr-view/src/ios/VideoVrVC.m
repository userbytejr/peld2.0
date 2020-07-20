//
//  VideoVrVC.m
//  GoogleVrViewTest
//
//  Created by Leonardo Tonghini on 12/05/17.
//  Copyright © 2017 Leonardo Tonghini. All rights reserved.
//

#import "VideoVrVC.h"

@interface VideoVrVC ()

@end


@implementation VideoVrVC

@synthesize videoType, videoURL;

- (instancetype)init {
    self = [super init];
    if (self) {
        videoType = kGVRVideoTypeMono;
    }
    return self;
}

-(void)setOptions: (NSMutableDictionary *) options {
    [super setOptions:options];
    videoType = [[options objectForKey:@"inputType"] intValue];
}

- (void)viewDidLoad {
    [super viewDidLoad];

    _videoView = [[GVRVideoView alloc] initWithFrame:self.view.frame];
    _widgetView = _videoView;

    [self.view addSubview:_videoView];

    _videoView.delegate = self;
    _videoView.enableFullscreenButton = YES;
    _videoView.enableCardboardButton = YES;
    _videoView.enableTouchTracking = YES;
    _videoView.enableInfoButton = NO;

    [_videoView loadFromUrl:videoURL ofType:videoType];
    [_videoView setDisplayMode:startDisplayMode];

    if(videoURL == nil) {
        [self showError:@"Error while loading resource"];
    }
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}


#pragma mark - GVRVideoViewDelegate

- (void)widgetViewDidTap:(GVRWidgetView *)widgetView {
    if (_isPaused) {
        [_videoView play];
        [self showPlayIndicatorState:NO];
    } else {
        [_videoView pause];
        [self showPlayIndicatorState:YES];
    }
    _isPaused = !_isPaused;
}

- (void)widgetView:(GVRWidgetView *)widgetView didLoadContent:(id)content {
    [super widgetView:widgetView didLoadContent:content];
    [_videoView play];
    _isPaused = NO;
}

- (void)videoView:(GVRVideoView*)videoView didUpdatePosition:(NSTimeInterval)position {
    // Loop the video when it reaches the end.
    if (position == videoView.duration) {
        [_videoView seekTo:0];
        [_videoView play];
    }
}

@end
