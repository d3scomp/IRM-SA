function makeGraph(varargin)
%% MAKEGRAPH reads specified file containing time series to be plotted.
% It accepts the following parameters:
% * Mandatory
%  * |fileName| - Name of the file to be processed
% * Optional - Key-Value pairs
%  * |rows| - Rows of data to be processed
%  * |labels| - List of labels of data to be plotted
%%
%% Examples
% *makeGraph('evaluationData.csv')*
% Reads the whole file specified and plots the default predefined data
% |temperature_distance|, |position_distance|.
%
% *makeGraph('evaluationData.csv', 'rows', 1:50)*
% Processes only first 50 rows of data (data header is not counted line,
% data lines start from 1)in the file specified and plots
% the default predefined data |temperature_distance|, |position_distance|.
%
% *makeGraph('evaluationData.csv', 'labels', {'actual_temperature',
% 'belief_temperature'})*
% Reads the whole file specified and plots the values of given data series
% |actual_temperature|, |belief_temperature|.
%%
    if(nargin < 1)
        error('No file name specified');
    else
        fileName = char(varargin(1));
    end
    if(nargin > 1)
        for i = 2:2:nargin
            key = char(varargin(i));
            if nargin < i+1
                error('No value specified for the argument "%s"', key);
            end
            switch key
                case 'rows'
                    if isnumeric(varargin{i+1})
                        rows = varargin{i+1};
                    else
                        error('Invalid value of "%s" argument', 'rows');
                    end
                case 'labels'
                    if iscellstr(varargin{i+1})
                        labels = varargin{i+1};
                    else
                        error('Invalid value of "%s" argument', 'labels');
                    end
                otherwise
                    error('Unknown argument "%s"', key);
            end
        end
    end
%%
    time = 'time';
    if exist('labels', 'var')
        dataToPlot = labels;
    else
        dataToPlot = {'temperature_distance', 'position_distance'};
    end
    if exist('rows', 'var')
        dataToProcess = rows;
    else
        dataToProcess = [];
    end
%%
    data = extractData(fileName, dataToProcess);

    if ~isKey(data, time)
        fprintf('Data for "%s" not found.\n', time);
        return;
    end
%%
    for j = 1:size(dataToPlot, 2)
        key = char(dataToPlot(j));
        if isKey(data, key)
            fprintf('Plotting "%s".\n', key);
            figure('name', key);
            plot(data(time), data(key));
            disp(data(key));
        else
            fprintf('"%s" data not found. Can''t plot them.\n', key);
        end
    end

end

function data = extractData(fileName, rows)
    separator = ';';
    data = containers.Map;

    f = fopen(fileName);
    fprintf('Opening file "%s".\n', fileName);

    % Obtain the data keys
    lineCnt = lineCount(f);
    line = fgetl(f);
    lineNum = 1;
    keys = strsplit(line, separator);
    for k = 1:size(keys, 2)
        key = char(keys(k));
        fprintf('The following key found: "%s".\n', key);
        data(key) = zeros(1, lineCnt-1); % -1 to exclude the header
    end

    % Load all the values
    line = fgetl(f);
    lineNum = lineNum + 1;
    while ischar(line)
        % Process the line
        values = strsplit(line, separator);
        if size(values, 2) ~= size(keys, 2)
            fprintf('Line %d is incomplete. Skipping the line.\n', lineNum);
        else
            for i = 1:size(keys, 2)
                % Process each column
                key = char(keys(i));
                v = data(key);
                value = str2double(values(i));
                v(lineNum-1) = value; % -1 to skip the header
                data(key) = v;
            end
        end
        line = fgetl(f);
        lineNum = lineNum + 1;
    end
    
    fclose(f);
    fprintf('Reading the file completed.\n');
    
    % Filter the wanted data
    if ~isempty(rows)
        for i = 1:size(keys, 2)
            % Process each column
            key = char(keys(i));
            v = data(key);
            data(key) = v(rows);
        end
    end
end

function cnt = lineCount(fid)
    n = 0;
    line = fgetl(fid);
    while ischar(line)
        n = n + 1;
        line = fgetl(fid);
    end
    
    frewind(fid);
    cnt = n;
end

% function distances = euclid(aP, bP)
% 
%     distances = ones(1, size(aP, 1));
% 
%     for i = 1:size(aP, 1)
%         distances(i) = sqrt((aP(i, 1) - bP(i, 1))^2 + (aP(i, 2) - bP(i, 2))^2);
%     end
% end