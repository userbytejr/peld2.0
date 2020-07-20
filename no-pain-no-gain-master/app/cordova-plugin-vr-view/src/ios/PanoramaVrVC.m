//
//  FotoVrVC.m
//  GoogleVrViewTest
//
//  Created by Leonardo Tonghini on 12/05/17.
//  Copyright © 2017 Leonardo Tonghini. All rights reserved.
//

#import "PanoramaVrVC.h"

@interface PanoramaVrVC ()

@end

@implementation PanoramaVrVC

@synthesize panoramaType, panoramaURL;

- (instancetype)init {
    self = [super init];
    if (self) {
        panoramaType = kGVRPanoramaImageTypeMono;
    }
    return self;
}

-(void)setOptions: (NSMutableDictionary *) options {
    [super setOptions:options];
    panoramaType = [[options objectForKey:@"inputType"] intValue];
}

- (void)viewDidLoad {
    [super viewDidLoad];

    _panoView = [[GVRPanoramaView alloc] init];
    _widgetView = _panoView;

    [self.view addSubview:_panoView];

    _panoView.delegate = self;
    _panoView.enableFullscreenButton = YES;
    _panoView.enableCardboardButton = YES;
    _panoView.enableTouchTracking = YES;
    _panoView.enableInfoButton = NO;

    [_panoView setDisplayMode:startDisplayMode];

    if(panoramaURL == nil) {
        [self showError:@"Error while loading resource"];
    } else {
        NSURLSessionTask *task = [[NSURLSession sharedSession] dataTaskWithURL:panoramaURL completionHandler:^(NSData * _Nullable data, NSURLResponse * _Nullable response, NSError * _Nullable error) {
            if(data) {
                UIImage *image = [UIImage imageWithData:data];
                if (image != nil) {
                    dispatch_async(dispatch_get_main_queue(), ^(void) {
                        [_panoView loadImage:image ofType:panoramaType];
                    });
                } else {
                    [self performSelectorOnMainThread:@selector(showError:) withObject:@"Error while loading resource" waitUntilDone:NO];
                }
            }
            else {
                [self performSelectorOnMainThread:@selector(showError:) withObject:@"Error while loading resource" waitUntilDone:NO];
            }
        }];
        [task resume];
    }
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

@end
